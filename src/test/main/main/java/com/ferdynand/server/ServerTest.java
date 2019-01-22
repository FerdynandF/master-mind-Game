package main.java.com.ferdynand.server;

import main.java.com.ferdynand.model.settings.SettingConnection;
import main.java.com.ferdynand.model.settings.SettingConnectionDao;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServerTest {

    @Test
    public void shouldRunServer() {
        Server server = new Server();
        Thread thread = new Thread(server);
        thread.start();
        assertTrue(thread.isAlive());

        thread.interrupt();
    }

    @Test
    public void shouldLoadSettingsFromXML(){
        Server server = new Server();
        Thread thread = new Thread(server);
        thread.start();
        SettingConnection settingConnection = SettingConnectionDao.getInstance().getSettingConnectionFromXMLFile("settingConnection.xml");

        assertEquals(settingConnection.getPlayersInRoom(), server.getSettingConnection().getPlayersInRoom());
        assertEquals(settingConnection.getPort(), server.getSettingConnection().getPort());
        assertEquals(settingConnection.getIp(), server.getSettingConnection().getIp());
    }

}