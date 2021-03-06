/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.uva.mlc.eurovoc.featureextractor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import nl.uva.mlc.eurovoc.EuroVocDoc;
import nl.uva.mlc.settings.Config;
import static nl.uva.mlc.settings.Config.configFile;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.SimpleFSDirectory;

/**
 *
 * @author mosi
 */
public class RawFeatureCalculator{

    static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(RawFeatureCalculator.class.getName());
    private ArrayList<Integer> featureNumbers = null;
    private FeaturesDefinition fd = null;
    private FeatureNormalizer fn = new FeatureNormalizer();
    private Integer qId=1;
    private String outDir;
    public RawFeatureCalculator() {

    }

    private void Init(){
                
        
        this.featureNumbers = new ArrayList<>();
        for(String s: Config.configFile.getProperty("FEATURE_NUMBERS").split(",")){
            this.featureNumbers.add(Integer.parseInt(s.trim()));
        }
        log.info("features to be calculated: " + this.featureNumbers.toString());
          try {
            File file = new File(this.outDir + "/all_folds_" + this.featureNumbers.toString() + ".txt");
            if (file.exists()) {
                file.delete();
                log.info("Deletting the existing directory on: " + this.outDir);
            }
           file.createNewFile();
        } catch (IOException ex) {
            log.error(ex);
        }
    }
        
    public void setFd(FeaturesDefinition fd) {
        this.fd = fd;
    }
    
    
    /**
     * <code>featureNumbers</code>: Features numbers are defined as follow: 1-
     * Retrieval based on Language Model using Dirichlet smoothing 2- Retrieval
     * based on Language Model using Jelinek Mercer smoothing 3- Retrieval based
     * on Okapi BM25
     *
     *
     *
     */

    
    
    
    public void Quering(EuroVocDoc docAsQuery) {
        TreeMap<Integer, HashMap<String, Feature>> allFeature_oneQ_allD = new TreeMap<Integer, HashMap<String, Feature>>();
        for (int fnum : featureNumbers) {
            allFeature_oneQ_allD.put(fnum,this.calculateFeatures(docAsQuery,fnum));
        }
        normalizeAndWriteToFile(docAsQuery, allFeature_oneQ_allD);
        log.info("All features has been calculated for document " + docAsQuery.getId());
        this.qId++;

    }

    public HashMap<String, Feature> calculateFeatures(EuroVocDoc docAsQuery, Integer fnum) {
       HashMap<String, Feature> allFeature_oneQ_allD = new HashMap<String, Feature>();
        HashMap<String, Feature> f = null;
        List<Float> params = null;
        switch (fnum) {
            case 1:
                params= Arrays.asList(2000F);
                f = fd.F_retrievalBased(docAsQuery,"TEXT", "TEXT", "LMD", params);
                allFeature_oneQ_allD.putAll(f);
                break;
            case 2:
                params= Arrays.asList(0.6F);
                f = fd.F_retrievalBased(docAsQuery,"TEXT", "TEXT", "LMJM", params);
                allFeature_oneQ_allD.putAll(f);
                break;
            case 3:
                params= Arrays.asList(0.75F);
                f = fd.F_retrievalBased(docAsQuery,"TEXT", "TEXT", "BM25", params);
                allFeature_oneQ_allD.putAll(f);
                break;
            case 4:
                params= Arrays.asList(2000F);
                f = fd.F_retrievalBased(docAsQuery, "TEXT", "TITLE", "LMD", params);
                allFeature_oneQ_allD.putAll(f);
                break;
            case 5:
                params= Arrays.asList(0.6F);
                f = fd.F_retrievalBased(docAsQuery, "TEXT", "TITLE", "LMJM", params);
                allFeature_oneQ_allD.putAll(f);
                break;
            case 6:
                params= Arrays.asList(0.75F);
                f = fd.F_retrievalBased(docAsQuery,"TEXT", "TITLE","BM25", params);
                allFeature_oneQ_allD.putAll(f);
                break;
            case 7:
                params= Arrays.asList(2000F);
                f = fd.F_retrievalBased(docAsQuery, "TEXT", "DESC","LMD", params);
                allFeature_oneQ_allD.putAll(f);
                break;
            case 8:
                params= Arrays.asList(0.6F);
                f = fd.F_retrievalBased(docAsQuery, "TEXT", "DESC", "LMJM", params);
                allFeature_oneQ_allD.putAll(f);
                break;
            case 9:
                params= Arrays.asList(0.75F);
                f = fd.F_retrievalBased(docAsQuery, "TEXT", "DESC","BM25", params);
                allFeature_oneQ_allD.putAll(f);
                break;
            case 10:
                params= Arrays.asList(2000F);
                f = fd.F_retrievalBased(docAsQuery, "TEXT", "CUMDESC","LMD", params);
                allFeature_oneQ_allD.putAll(f);
                break;
            case 11:
                params= Arrays.asList(0.6F);
                f = fd.F_retrievalBased(docAsQuery, "TEXT", "CUMDESC", "LMJM", params);
                allFeature_oneQ_allD.putAll(f);
                break;
            case 12:
                params= Arrays.asList(0.75F);
                f = fd.F_retrievalBased(docAsQuery, "TEXT", "CUMDESC", "BM25", params);
                allFeature_oneQ_allD.putAll(f);
                break;
            case 13:
                params= Arrays.asList(1000F);
                f = fd.F_retrievalBased(docAsQuery,"TITLE", "TEXT", "LMD", params);
                allFeature_oneQ_allD.putAll(f);
                break;
            case 14:
                params= Arrays.asList(0.2F);
                f = fd.F_retrievalBased(docAsQuery,"TITLE", "TEXT", "LMJM", params);
                allFeature_oneQ_allD.putAll(f);
                break;
            case 15:
                params= Arrays.asList(0.65F);
                f = fd.F_retrievalBased(docAsQuery,"TITLE", "TEXT", "BM25", params);
                allFeature_oneQ_allD.putAll(f);
                break;
            case 16:
                params= Arrays.asList(1000F);
                f = fd.F_retrievalBased(docAsQuery, "TITLE", "TITLE", "LMD", params);
                allFeature_oneQ_allD.putAll(f);
                break;
            case 17:
                params= Arrays.asList(0.2F);
                f = fd.F_retrievalBased(docAsQuery, "TITLE", "TITLE", "LMJM", params);
                allFeature_oneQ_allD.putAll(f);
                break;
            case 18:
                params= Arrays.asList(0.65F);
                f = fd.F_retrievalBased(docAsQuery,"TITLE", "TITLE","BM25", params);
                allFeature_oneQ_allD.putAll(f);
                break;
            case 19:
                params= Arrays.asList(1000F);
                f = fd.F_retrievalBased(docAsQuery, "TITLE", "DESC","LMD", params);
                allFeature_oneQ_allD.putAll(f);
                break;
            case 20:
                params= Arrays.asList(0.2F);
                f = fd.F_retrievalBased(docAsQuery, "TITLE", "DESC", "LMJM", params);
                allFeature_oneQ_allD.putAll(f);
                break;
            case 21:
                params= Arrays.asList(0.65F);
                f = fd.F_retrievalBased(docAsQuery, "TITLE", "DESC","BM25", params);
                allFeature_oneQ_allD.putAll(f);
                break;
            case 22:
                params= Arrays.asList(1000F);
                f = fd.F_retrievalBased(docAsQuery, "TITLE", "CUMDESC", "LMD", params);
                allFeature_oneQ_allD.putAll(f);
                break;
            case 23:
                params= Arrays.asList(0.2F);
                f = fd.F_retrievalBased(docAsQuery, "TITLE", "CUMDESC", "LMJM", params);
                allFeature_oneQ_allD.putAll(f);
                break;
            case 24:
                params= Arrays.asList(0.65F);
                f = fd.F_retrievalBased(docAsQuery, "TITLE", "CUMDESC", "BM25", params);
                allFeature_oneQ_allD.putAll(f);
                break;
            case 25:
                params= Arrays.asList(1000F);
                f = fd.F_retrievalBased(docAsQuery, "NAMEDENTITIES", "NAMEDENTITIES", "LMD", params);
                allFeature_oneQ_allD.putAll(f);
                break;
            case 26:
                params= Arrays.asList(0.2F);
                f = fd.F_retrievalBased(docAsQuery, "NAMEDENTITIES", "NAMEDENTITIES", "LMJM", params);
                allFeature_oneQ_allD.putAll(f);
                break;
            case 27:
                params= Arrays.asList(0.65F);
                f = fd.F_retrievalBased(docAsQuery, "NAMEDENTITIES", "NAMEDENTITIES", "BM25", params);
                allFeature_oneQ_allD.putAll(f);
                break;
            case 28:
                f = fd.F_classDegreeInHierarchy("p");
                allFeature_oneQ_allD.putAll(f);
                break;
            case 29:
                f = fd.F_classDegreeInHierarchy("c");
                allFeature_oneQ_allD.putAll(f);
                break;
            case 30:
                f = fd.F_classDocNum();
                allFeature_oneQ_allD.putAll(f);
                break;
            case 31:
                f = fd.F_classLevelInHierarchy();
                allFeature_oneQ_allD.putAll(f);
                break;
            default:
                log.info("Not valid feature number: " + fnum);
        }
        return allFeature_oneQ_allD;
        
    }
    
    private void testIndexDocReader() {
        try {
            IndexReader testIreader = IndexReader.open(new SimpleFSDirectory(new File(configFile.getProperty("TEST_INDEX_PATH"))));
            for (int i = 0; i < testIreader.numDocs(); i++) {
                String id = testIreader.document(i).get("ID");
                String title = testIreader.document(i).get("TITLE");
                String text = testIreader.document(i).get("TEXT");
                String namedEntities = testIreader.document(i).get("NAMEDENTITIES");
                String[] classes = testIreader.document(i).get("CLASSES").split("\\s+");
                EuroVocDoc doc = new EuroVocDoc(id, title, text, namedEntities, new ArrayList<String>(Arrays.asList(classes)));
                Quering(doc);
                log.info(i + " from " + testIreader.numDocs());
            }
        } catch (IOException ex) {
            log.error(ex);
        }
    }


    public void conceptBaseFeatureCalc() {
       
       try {
            IndexReader trainIreader = IndexReader.open(new SimpleFSDirectory(new File(configFile.getProperty("CONCEPT_INDEX_PATH"))));
            this.fd = new FeaturesDefinition(trainIreader);
            this.testIndexDocReader();
        } catch (IOException ex) {
            log.error(ex);
        }
    }

//    public void docBaseFeatureCalc() {
//        try {
//            String queriesPath = configFile.getProperty("CORPUS_Eval_PATH");
//            IndexReader ireader = IndexReader.open(new SimpleFSDirectory(new File(configFile.getProperty("DOC_INDEX_PATH"))));
//            this.fd = new FeaturesDefinition(ireader);
//            fileReader(new File(queriesPath));
//        } catch (IOException ex) {
//            log.error(ex);
//        }
//    }

    private void normalizeAndWriteToFile(EuroVocDoc docAsQuery, TreeMap<Integer, HashMap<String, Feature>> allFeature_oneQ_allD) {
        HashMap<String, TreeMap<Integer,Feature>> docs = new HashMap<>();
        TreeMap<Integer, HashMap<String, Feature>> allFeature_oneQ_allD_Normalized = this.fn.oneQueryNormalizer(allFeature_oneQ_allD);
        for(Map.Entry<Integer, HashMap<String, Feature>> ent: allFeature_oneQ_allD_Normalized.entrySet()){
            Integer fnum = ent.getKey();
            for(Map.Entry<String, Feature> ent2: ent.getValue().entrySet()){
                TreeMap<Integer,Feature> fs = docs.get(ent2.getKey());
                if(fs==null)
                    fs = new TreeMap<Integer,Feature>();
                fs.put(fnum, ent2.getValue());
                docs.put(ent2.getKey(), fs);
            }
        }
        try{
        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(this.outDir +"/all_folds_"+this.featureNumbers.toString() +".txt",true)));
        for(Map.Entry<String, TreeMap<Integer,Feature>> ent: docs.entrySet()){

                String lbl = (docAsQuery.getClasses().contains(ent.getKey()))? "1" : "0";
                String docId = ent.getKey();
                String qName = docAsQuery.getId();
                String tmpLine = "";
                for(int fnum:this.featureNumbers){
                    if(!ent.getValue().containsKey(fnum)){
                        ent.getValue().put(fnum, new Feature(qName, 0D, qName, docId, lbl));
                    }
                }
                Integer fId=0;
                for(Map.Entry<Integer,Feature> ent2: ent.getValue().entrySet()){
                        tmpLine += (++fId) + ":" + ent2.getValue().getfValue().toString() + " ";
                }
                String line =  lbl+ " "
                            + "qid:" + qId + " "
                            + tmpLine 
                            + "# "
                            + qName + " "
                            + docId
                            +"\n";
                pw.write(line);
            }
        pw.close();
        }catch(IOException ex){
            log.error(ex);
        }    }
     public void main(String outDir){
        this.outDir = outDir;
        this.Init();
        this.conceptBaseFeatureCalc();
    }
}
