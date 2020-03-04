# CS4225P1Group1
In order for this project to run you must have javaFx 11 installed.

Right click on the client project, go to properties, go to java build path, go to libraries, click the modulepath. Click the javaFx library and click edit. Click user libraries and select all of the javafx jar files. Then apply and close.

Next right click on the client project, select run as, select run configurations and add the below lines to the VM arguments. Your module path will be different. You have to find where the javafx lib folder is located on your computer.

--module-path="C:\Users\lucas\eclipse\java-2019-06\eclipse\javafx-sdk-11.0.2\lib" --add-modules=javafx.controls,javafx.fxml
