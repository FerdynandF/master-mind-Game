package settings;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class SettingConnectionDao {
    private static SettingConnectionDao instance;

    protected SettingConnectionDao(){}

    public static SettingConnectionDao getInstance(){
        if(instance == null){
            synchronized (SettingConnectionDao.class) {
                if(instance == null) {
                    instance = new SettingConnectionDao();
                }
            }
        }
        return instance;
    }

    public SettingConnection getSettingConnectionFromXMLFile(String url){
        try {
            File file = new File(url);
            JAXBContext jaxbContext = JAXBContext.newInstance(SettingConnection.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            return (SettingConnection) unmarshaller.unmarshal(file);
        } catch (JAXBException e) {
            e.getMessage();
        }
        return null;
    }
}

