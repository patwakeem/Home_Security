package com.interactivehome.main_service.repository;

import com.interactivehome.main_service.model.dto.*;
import org.springframework.stereotype.Repository;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;

@Repository
public class MainRepository {
    public void storeDoorState(DoorSensorDTO dto) {
        try {
            File myFile = new File("/var/www/html/data/door_state.dat");
            if (myFile.createNewFile()) {
                System.out.println("File created: " + myFile.getName());
            } else {
                System.out.println("File already exists.");
            }
            FileWriter myWriter = new FileWriter("/var/www/html/data/door_state.dat");
            if(dto.getDoorState()) {
                myWriter.write("1");
            } else {
                myWriter.write("0");
            }
            myWriter.close();
            System.out.println("Successfully stored the door state: " + dto.getDoorState().toString());
        } catch (IOException e) {
            System.out.println("An error occurred while storing the door state.");
            e.printStackTrace();
        }
    }

    public void storeGasValue(SmokeSensorDTO dto) {
        try {
            File myFile = new File("/var/www/html/data/gas_value.dat");
            if (myFile.createNewFile()) {
                System.out.println("File created: " + myFile.getName());
            } else {
                System.out.println("File already exists.");
            }
            FileWriter myWriter = new FileWriter("/var/www/html/data/gas_value.dat");
            myWriter.write(dto.getGasValue().toString());
            myWriter.close();
            System.out.println("Successfully stored the gas value: " + dto.getGasValue().toString());
        } catch (IOException e) {
            System.out.println("An error occurred while storing the gas value.");
            e.printStackTrace();
        }
    }

    public void storeTemperature(ThermometerSensorDTO dto) {
        try {
            File myFile = new File("/var/www/html/data/temperature.dat");
            if (myFile.createNewFile()) {
                System.out.println("File created: " + myFile.getName());
            } else {
                System.out.println("File already exists.");
            }
            FileWriter myWriter = new FileWriter("/var/www/html/data/temperature.dat");
            myWriter.write(dto.getTemperature().toString());
            myWriter.close();
            System.out.println("Successfully stored the gas value: " + dto.getTemperature().toString());
        } catch (IOException e) {
            System.out.println("An error occurred while storing the gas value.");
            e.printStackTrace();
        }
    }

    public String getTemperature() {
        try {
            File myObj = new File("/var/www/html/data/temperature.dat");
            Scanner myReader = new Scanner(myObj);
            String data = new String();
            while (myReader.hasNextLine()) {
                data = myReader.nextLine();
            }
            myReader.close();
            return data;
        } catch (FileNotFoundException e) {
            System.out.println("The file '/var/www/html/data/temperature.dat' does not exist!");
            e.printStackTrace();
        }
        return "";
    }

    public void storeHumidityValue(HumiditySensorDTO dto) {
        try {
            File myFile = new File("/var/www/html/data/humidity.dat");
            if (myFile.createNewFile()) {
                System.out.println("File created: " + myFile.getName());
            } else {
                System.out.println("File already exists.");
            }
            FileWriter myWriter = new FileWriter("/var/www/html/data/humidity.dat");
            myWriter.write(dto.getHumidity().toString());
            myWriter.close();
            System.out.println("Successfully stored the gas value: " + dto.getHumidity().toString());
        } catch (IOException e) {
            System.out.println("An error occurred while storing the gas value.");
            e.printStackTrace();
        }
    }

    public void storePersonVerified(PersonVerifiedDTO dto) {
        try {
            File myFile = new File("/var/www/html/data/person_verified.dat");
            if (myFile.createNewFile()) {
                System.out.println("File created: " + myFile.getName());
            } else {
                System.out.println("File already exists.");
            }
            FileWriter myWriter = new FileWriter("/var/www/html/data/person_verified.dat");
            myWriter.write(dto.getPerson_verified().toString());
            myWriter.close();
            System.out.println("Successfully stored the person_verified value: " + dto.getPerson_verified().toString());
        } catch (IOException e) {
            System.out.println("An error occurred while storing the person_verified value.");
            e.printStackTrace();
        }
    }

    public void storeAlarmState(AlarmDTO alarmDTO) {
        try {
            File myFile = new File("/var/www/html/data/alarm_state.dat");
            if (myFile.createNewFile()) {
                System.out.println("File created: " + myFile.getName());
            } else {
                System.out.println("File already exists.");
            }
            FileWriter myWriter = new FileWriter("/var/www/html/data/alarm_state.dat");
            myWriter.write(alarmDTO.getAlarm_state().toString());
            myWriter.close();
            System.out.println("Successfully stored the alarm_state value: " + alarmDTO.getAlarm_state());
        } catch (IOException e) {
            System.out.println("An error occurred while storing the alarm_state value.");
            e.printStackTrace();
        }
    }

    public String getAlarmState() {
        try {
            File myObj = new File("/var/www/html/data/alarm_state.dat");
            Scanner myReader = new Scanner(myObj);
            String data = new String();
            while (myReader.hasNextLine()) {
                data = myReader.nextLine();
            }
            myReader.close();
            return data;
        } catch (FileNotFoundException e) {
            System.out.println("The file '/var/www/html/data/alarm_state.dat' does not exist!");
            e.printStackTrace();
        }
        return "";
    }

    public void storeAlarmOn(AlarmDTO alarmDTO) {
        try {
            File myFile = new File("/var/www/html/data/alarm_on.dat");
            if (myFile.createNewFile()) {
                System.out.println("File created: " + myFile.getName());
            } else {
                System.out.println("File already exists.");
            }
            FileWriter myWriter = new FileWriter("/var/www/html/data/alarm_on.dat");
            if(alarmDTO.getAlarm_on()) {
                myWriter.write("1");
            } else {
                myWriter.write("0");
            }
            myWriter.close();
            System.out.println("Successfully stored the alarm_state value: " + alarmDTO.getAlarm_state());
        } catch (IOException e) {
            System.out.println("An error occurred while storing the alarm_on value.");
            e.printStackTrace();
        }
    }

    public String getAlarmOn() {
        try {
            File myObj = new File("/var/www/html/data/alarm_on.dat");
            Scanner myReader = new Scanner(myObj);
            String data = new String();
            while (myReader.hasNextLine()) {
                data = myReader.nextLine();
            }
            myReader.close();
            return data;
        } catch (FileNotFoundException e) {
            System.out.println("The file '/var/www/html/data/alarm_on.dat' does not exist!");
            e.printStackTrace();
        }
        return "";
    }
}
