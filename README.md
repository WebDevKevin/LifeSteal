# LifeStealPlugin
Minecraft server plugin for a Survival Multiplayer environment.  If a player dies by another player's hand, they lose a heart and it is transferred to the player who killed them.  Once any player has no hearts left they are banned permanently. 

## Platform Information
- Tested on PaperMC (https://papermc.io/) version 1.18.1
- Java version: OpenJDK 17
- Dev Platform: IntelliJ Community Edition

## Installation
1. Using IntelliJ open the pom.xml file
2. Click the icon to import Maven dependencies (this may take a few moments)
3. Expand the Maven tab on the top right to access Maven commands
4. Expand "LifeStealPlugin" > "Lifecycle" and double click on "Package"
5. Check the console for errors. If there are no errors look in your local project folder for a new folder called "target" This is where you will find the compiled jar file.
6. Place compiled jar (LifeStealPlugin-1.0.jar) in the server "plugins" folder and restart:

    
    Example:
    /opt/minecraft/paper/plugins/LifeStealPlugin-1.0.jar

## Usage:
Note: This plugin is only available to Operators

    Enable plugin: 
    /lifesteal on

    Disable plugin:
    /lifesteal off

    Get plugin status:
    /lifesteal status

    Set max hearts for all players who use the server (default is 10). Note this only affects users who have never joined.  See "resetuser" command for alternative.
    /lifesteal defaulthearts ##

    Resets player heart count to default.  Useful if heart count was changed and this user has already been on the server.
    /lifesteal resetuser USERNAME

