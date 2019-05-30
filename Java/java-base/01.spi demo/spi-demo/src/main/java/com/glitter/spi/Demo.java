package com.glitter.spi;

import java.util.ServiceLoader;

public class Demo{

    public static void main(String[] args){
        carInterface();
        writeInterface();
    }


    private static void carInterface(){
        ServiceLoader<CarInterface> carInterfaceLoader = ServiceLoader.load(CarInterface.class);
        for (CarInterface carInterface : carInterfaceLoader) {
            carInterface.run();
            carInterface.stop();
        }
    }

    private static void writeInterface(){
        ServiceLoader<WriteInterface> writeInterfaceLoader = ServiceLoader.load(WriteInterface.class);
        for (WriteInterface writeInterface : writeInterfaceLoader) {
            writeInterface.write("hello world!");
        }
    }


}
