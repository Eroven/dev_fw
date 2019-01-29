package me.zhaotb.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.ListCollectionsIterable;
import com.mongodb.client.ListDatabasesIterable;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class Test {

    public static void main(String[] args) {
        ServerAddress addr = new ServerAddress("localhost", 27017);
        MongoCredential cre = MongoCredential.createCredential("test", "admin", "123456".toCharArray());
        MongoClient client = new MongoClient(addr, cre, MongoClientOptions.builder().build());

        ListDatabasesIterable<Document> databases = client.listDatabases();
        for (Document database : databases) {
            System.out.println(database.toJson());
        }
        MongoDatabase db = client.getDatabase("test");
        ListCollectionsIterable<Document> documents = db.listCollections();

        for (Document doc : documents) {

            System.out.println(doc.toJson());
        }

//        DB fsDB = new DB(client, "test");
//        GridFS fs = new GridFS(fsDB);
//        List<GridFSDBFile> gFile = fs.find("mongodb-win32-x86_64-2008plus-ssl-4.0.2-signed.msi");
//        for (GridFSDBFile f : gFile) {
//            InputStream inputStream = f.getInputStream();
//            try {
//                FileOutputStream outputStream = new FileOutputStream("F:\\mongodb.msi");
//                int copy = StreamUtils.copy(inputStream, outputStream);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        File f = new File("F:\\download_20181010\\mongodb-win32-x86_64-2008plus-ssl-4.0.2-signed.msi");
//        try {
//            GridFSInputFile file = fs.createFile(f);
//            String filename = file.getFilename();
//            System.out.println(filename);
//            file.save();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }
}
