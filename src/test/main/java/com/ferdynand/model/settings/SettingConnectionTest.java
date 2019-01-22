package main.java.com.ferdynand.model.settings;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SettingConnectionTest {

    @Test
    public void shouldReadAllConnectionSettingFromXMLFile(){
        SettingConnection settingConnection = SettingConnectionDao.getInstance().getSettingConnectionFromXMLFile("settingConnection.xml");
        assertEquals("127.0.0.1", settingConnection.getIp());
        assertEquals(5005, settingConnection.getPort());
        assertEquals(2, settingConnection.getPlayersInRoom());
    }
}
