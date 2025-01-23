import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class WeatherMining {

    public static class WeatherMapper extends Mapper<Object, Text, Text, Text> {
        private Text month = new Text();
        private Text values = new Text();

        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            String[] fields = line.split(",");

            if (!fields[0].equals("date")) { // Skip header
                String date = fields[0];
                String tempMax = fields[1];
                String tempMin = fields[2];
                String temp = fields[3];
                String humidity = fields[4];

             // Split the date in DD-MM-YYYY format to extract YYYY-MM
                String[] dateParts = date.split("-");
                String monthKey = dateParts[2] + "-" + dateParts[1]; // YYYY-MM format

                month.set(monthKey);
                values.set(tempMax + "," + tempMin + "," + temp + "," + humidity);
                context.write(month, values);
            }
        }
    }

    public static class WeatherReducer extends Reducer<Text, Text, Text, Text> {
        public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            double maxTemp = Double.MIN_VALUE; 
            double minTemp = Double.MAX_VALUE; 
            double totalTemp = 0;
            double totalHumidity = 0;
            int count = 0;

            for (Text value : values) {
                String[] fields;
                try {
                    fields = value.toString().split(",");
                    double tempMax = Double.parseDouble(fields[0]);
                    double tempMin = Double.parseDouble(fields[1]);
                    double temp = Double.parseDouble(fields[2]);
                    double humidity = Double.parseDouble(fields[3]);

                    // Update max and min temperatures
                    if (tempMax > maxTemp) maxTemp = tempMax;
                    if (tempMin < minTemp) minTemp = tempMin;

                    totalTemp += temp;
                    totalHumidity += humidity;
                    count++;
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing temperature data: " + e.getMessage());
                }
            }

            // Perform the calculations only if count > 0
            if (count > 0) {
                double avgTemp = totalTemp / count;
                double avgHumidity = totalHumidity / count;

                // Format output as required
                String result = String.format("Max Temp: %.0f, Min Temp: %.0f, Avg Temp: %.2f, Avg Humidity: %.1f",
                        maxTemp, minTemp, avgTemp, avgHumidity);
                context.write(new Text(key.toString()), new Text(result)); // Using key as month (YYYY-MM)
            }
        }
    }


    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Weather Mining");
        job.setJarByClass(WeatherMining.class);
        job.setMapperClass(WeatherMapper.class);
        job.setReducerClass(WeatherReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}