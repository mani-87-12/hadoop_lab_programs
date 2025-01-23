# Hadoop Projects

This repository contains two separate projects related to Hadoop:

1. **Matrix Multiplication**
2. **Weather Mining**

## 1. Matrix Multiplication

The Matrix Multiplication project demonstrates how to perform matrix multiplication using Hadoop's MapReduce framework. The project includes both the Mapper and Reducer code, which processes large datasets and computes the result using distributed computing.

### Key Features:
- MapReduce-based implementation of matrix multiplication
- Efficient handling of large matrices
- Scalable across multiple nodes in a Hadoop cluster

### How to Run:
1. Compile the Java code.
2. Package it into a JAR file.
3. Run the job using the Hadoop command line interface.

## 2. Weather Mining

The Weather Mining project processes weather data for the year 2024, using Hadoop to analyze the daily weather records. The project includes MapReduce code to compute various weather-related metrics from the dataset, such as temperature and humidity averages.

### Key Features:
- Weather data analysis using Hadoop MapReduce
- Includes various weather statistics computations (temperature, humidity, etc.)
- Data from daily weather reports for the year 2024

### How to Run:
1. Prepare the input CSV file (`/mylavaram_weather_data_2024.csv`).
2. Compile the Java code.
3. Package it into a JAR file and run the job using Hadoop.

## Setup Instructions

### Prerequisites:
- Hadoop (version 3.x)
- Java (version 8 or 11)

### How to Set Up:
1. Install Hadoop and Java.
2. Copy the necessary files to your Hadoop directory.
3. Run the MapReduce jobs as described above.


