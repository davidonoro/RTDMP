package com.pebd.rtdmp;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.Message;
import org.kie.api.io.KieResources;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.StatelessKieSession;

import java.io.File;
import java.io.Serializable;

/**
 * Created by david on 13/12/15.
 */
public class KieSessionFactory implements Serializable{

    static StatelessKieSession statelessKieSession;
    static File rulesFile;


    /**
     * Devuelve una sesion sin estado contra el servidor de drools
     * @param filename
     * @return
     */
    public static StatelessKieSession getKieSession(String filename) {
        File newFile = new File(filename);
        boolean fileHasChanged = (rulesFile == null || rulesFile.lastModified() != newFile.lastModified());

        // Crea una nueva sesion si no existe o el fichero de reglas ha cambiado
        if (statelessKieSession == null || fileHasChanged) {
            rulesFile = new File(filename);
            statelessKieSession = getNewKieSession(filename);
        }

        return statelessKieSession;

    }

    /**
     * Crea una nueva sesion a partir del fichero
     * @param filename
     * @return
     */
    public static StatelessKieSession getNewKieSession(String filename) {
        System.out.println("creating a new kie session");

        KieServices kieServices = KieServices.Factory.get();
        KieResources kieResources = kieServices.getResources();

        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        KieRepository kieRepository = kieServices.getRepository();

        Resource resource = kieResources.newFileSystemResource(filename);
        kieFileSystem.write(resource);

        KieBuilder kb = kieServices.newKieBuilder(kieFileSystem);

        kb.buildAll();

        if (kb.getResults().hasMessages(Message.Level.ERROR)) {
            throw new RuntimeException("Build Errors:\n"
                    + kb.getResults().toString());
        }

        KieContainer kContainer = kieServices.newKieContainer(kieRepository.getDefaultReleaseId());
        return kContainer.newStatelessKieSession();
    }
}
