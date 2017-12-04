package com.ing.direct.common;

import com.mongodb.MongoClient;
import de.flapdoodle.embed.mongo.MongodExecutable;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractITest {

    protected void initMongoServer(MongodExecutable staticMongodExec,
                                   MongodExecutable mongodExec,
                                   MongoClient staticMongoClient,
                                   MongoClient mongoClient) {
        if(staticMongodExec == null) {
            staticMongodExec = mongodExec;
        }
        if(staticMongoClient == null) {
            staticMongoClient = mongoClient;
        }
    }

    public static void stopMongoServer(MongodExecutable mongodExec, MongoClient mongoClient) {
        mongoClient.close();
        mongodExec.stop();
    }

}
