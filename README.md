#### DingNet-ParkingManagement
This repository enables the parking management functionality in DingNet exemplar (https://www.hpi.uni-potsdam.de/giese/public/selfadapt/exemplars/dingnet/).

* To run this program, please perform the following:
    - you need to have IntelliJ installed on the system
    - you need to have an Internet connection for the DingNet application to load the city map
    - please clone the repo using the command "git clone https://github.com/am-i-helpful/DingNet-ParkingManagement.git"
    - please open the directory as an IntelliJ project, so that all configurations are appropriately loaded
    - please make sure that you've these 2 files in their specified directory (after cloning), or manually recreate using GitHub link (please do preserve the directory structure):
        - DingNet\.idea\workspace.xml (required for run configuration to load in IntelliJ)
        - DingNet\out\production\inputProfiles\inputProfile.xml (required for DingNet adaptation profile to work)
    - run DingNet (Java) application first in IntelliJ, select the required configurations and then execute "python main.py" from terminal/cmd, and you can see the output with simulation results.

* As a helpful suggestion, please check our DingNet video (available here) for a guide to understand how to run DingNet and the external simulator
