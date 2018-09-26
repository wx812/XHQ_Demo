package com.xhq.demo.db.db_sql.entityConfig.ModelConfig;

//import com.smzw.config.UtilDocument.UtilXml;
//import com.smzw.config.UtilPub;
//
//import org.dom4j.Document;
//import org.dom4j.Element;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FilenameFilter;
//import java.text.MessageFormat;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Map;
//import java.util.Set;
//
///**
// * 所有的实体定义；含库清单
// * 库名中sys对应所配置的数据源，其他一律为jt_[库名].
// */
//@SuppressWarnings("UnusedDeclaration")
//public class ModelReader {
//    private Map<String, ModelEntity> abstractEntitydefs = null;  //抽象实体定义集合；(key=abstract-entity-name(大写))
//    private Map<String, ModelEntity> entitydefs = null;  //实体定义集合；(key=ENTITY_NAME(大写))
//    public static String ENTITY_CONFIG_PATH = "entity/entitydef/";
//
//
//    //读取所有实体定义XML；
//    public ModelReader buildModelReader() {
//        File dir = new File(ENTITY_CONFIG_PATH);
//        File[] files = dir.listFiles(new FilenameFilter() {
//            public boolean accept(File dir, String name) {
//                name = name.toLowerCase();
//                return name.endsWith(".xml") && name.startsWith("entity-");
//            }
//        });
//
//        this.abstractEntitydefs = new HashMap<>();
//        this.entitydefs = new HashMap<>();
//        System.out.println("**********解析抽象实体定义**************************************");
//        for (File f : files) {
//            //实体定义文件，例如：entity-sys.xml
//            try {
//                readAbstractEntityDefFile(f.getName());
//            } catch (Exception e) {
//                System.out.println("解析实体定义文件出错 = " + f.getName());
//                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//            }
//        }
//        //循环实体定义文件（要和抽象实体定义进行合并，形成真正的实体定义）
//        System.out.println("**********解析实体定义（同时根据抽象实体定义进行合并）**********************");
//        for (File f : files) {
//            //实体定义文件，例如：entity-sys.xml
//            try {
//                readEntityDefFile(f.getName());
//            } catch (Exception e) {
//                System.out.println("解析实体定义文件出错 = " + f.getName());
//                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//            }
//        }
//        return this;
//    }
//
//    //解析抽象实体定义；
//    private void readAbstractEntityDefFile(String filename) throws Exception {
//        String xmlPath = ENTITY_CONFIG_PATH + filename;
//        FileInputStream in = new FileInputStream(xmlPath);
//        final Document document = UtilXml.readXmlDocument(in);
//        final Element rootElement = document.getRootElement();
//        //获取所有entity定义节点；
//        for (Iterator i = rootElement.elementIterator("abstract-entity"); i.hasNext(); ) {
//            Element entityelement = (Element) i.next();
//            ModelEntity absEntity = new ModelEntity(true);
//            absEntity.setEntityName(UtilXml.getAttribute(entityelement, "abstract-entity-name").toUpperCase());
//            //抽象实体定义本身不进行合并；
//	        String anExtends = UtilXml.getAttribute(entityelement, "extends");
//	        if (anExtends != null && anExtends.length() > 0)
//                 absEntity.setExtendsEntityNames(anExtends.toUpperCase().split(","));
//	        absEntity.setTitle(UtilXml.getAttribute(entityelement, "title"));
//            absEntity.setVersion(UtilXml.getAttribute(entityelement, "version"));
////            System.out.println(MessageFormat.format("abstract entity: name={0};extends={1};", absEntity.getEntityName(), absEntity.getExtendsEntityString()));
//            //开始读取所有字段定义；entity.fields
//            for (Iterator ec = entityelement.elementIterator(); ec.hasNext(); ) {
//                Element entitychild = (Element) ec.next();
//                String nodename = entitychild.getName();
//                switch (nodename) {
//                    case "field":
//                        ModelField f = new ModelField();
//                        f.name = UtilXml.getAttributeIgnoreNull(entitychild, "name").toLowerCase();
//                        f.type = UtilXml.getAttributeIgnoreNull(entitychild, "type");
//                        f.defaultValue = UtilXml.getAttributeIgnoreNull(entitychild, "defaultValue");
//                        f.description = UtilXml.getAttributeIgnoreNull(entitychild, "description");
//                        f.link = UtilXml.getAttributeIgnoreNull(entitychild, "link");
//                        if (UtilPub.isEmpty(f.title)) f.title = f.description;
//                        if (UtilPub.isEmptyStr(f.defaultValue)) {
//                            final ModelFieldType fieldType = f.getModelFieldType();
//                            if (fieldType != null) {
//                                f.defaultValue = fieldType.defaultValue;
//                            } else {
////                                UtilLogger.error("字段" + absEntity.getEntityName() + "." + f.name + "类型错误" + f.type);
//                            }
//                        }
//                        absEntity.addField(f);
//                        break;
//                    case "prim-key":
//                        //主键定义；
//                        String fieldname = UtilXml.getAttributeIgnoreNull(entitychild, "field").toLowerCase();
//                        if (absEntity.getField(fieldname) == null) {
//                            System.out.println(MessageFormat.format("抽象实体[{0}]主键定义的字段[{1}]在定义表中不存在!", absEntity.getEntityName(), fieldname));
//                            continue;
//                        }
//                        absEntity.getField(fieldname).isPk = true;
//                        break;
//                    case "index":
//                        //增加索引定义；
//                        ModelIndex modelIndex = new ModelIndex(absEntity, entitychild);
//                        absEntity.addIndex(modelIndex);
//                        break;
//                }
//            }
//            absEntity.updatePkLists();
//            if (this.abstractEntitydefs.get(absEntity.getEntityName()) != null) {
//                String message = MessageFormat.format("抽象实体定义 = {0}已经存在，不能重复定义!", absEntity.getEntityName());
//                System.out.println(message);
//                throw new Exception(message);
//            }
//            this.abstractEntitydefs.put(absEntity.getEntityName(), absEntity);
//        }
//    }
//
//    /**
//     * 读取一个实体定义文件；
//     * 请注意：如果继承的是非抽象表，必须保证被继承表定义在前
//     *
//     * @param filename
//     * @throws Exception
//     */
//    private void readEntityDefFile(String filename) throws Exception {
//        String xmlPath = ENTITY_CONFIG_PATH + filename;
//        FileInputStream in = new FileInputStream(xmlPath);
//        final Document document = UtilXml.readXmlDocument(in);
//        final Element rootElement = document.getRootElement();
//        final boolean autoCreated = UtilPub.toBoolean(UtilPub.checkEmpty(UtilXml.getAttribute(rootElement, "autocreated"), "1"), true);
//        final boolean recordchange = UtilPub.toBoolean(UtilPub.checkEmpty(UtilXml.getAttribute(rootElement, "recordchange"), "0"), false);
//        String schema = UtilPub.checkNull(UtilXml.getAttribute(rootElement, "schema"));
//        //遍历所有entity定义节点；
//        for (Iterator e = rootElement.elementIterator("entity"); e.hasNext(); ) {
//            Element entityelement = (Element) e.next();
//            ModelEntity modelEntity = new ModelEntity(false);
//            modelEntity.setAutoCreated(UtilPub.toBoolean(UtilPub.checkEmpty(UtilXml.getAttribute(entityelement, "autocreated"), null), autoCreated));
//            modelEntity.setRecordChange(UtilPub.toBoolean(UtilPub.checkEmpty(UtilXml.getAttribute(entityelement, "recordchange"), null), recordchange));
//            final String es = UtilPub.checkEmpty(UtilXml.getAttribute(entityelement, "schema"), schema);
//            modelEntity.setSchema(es);
//            modelEntity.setEntityName(UtilXml.getAttributeIgnoreNull(entityelement, "entity-name").toUpperCase());
//            //继承多个抽象实体，采用逗号进行分割；
//            modelEntity.setExtendsEntityNames(UtilXml.getAttributeIgnoreNull(entityelement, "extends").toUpperCase().split(","));
//            modelEntity.setTitle(UtilXml.getAttributeIgnoreNull(entityelement, "title"));
//            modelEntity.setVersion(UtilXml.getAttributeIgnoreNull(entityelement, "version"));
////            System.out.println(MessageFormat.format("entity: name={0};extends={1};", modelEntity.getEntityName(), modelEntity.getExtendsEntityString()));
//
//            //开始读取所有字段定义；entity.fields
//            for (Iterator ec = entityelement.elementIterator(); ec.hasNext(); ) {
//                Element entitychild = (Element) ec.next();
//                String nodename = entitychild.getName();
//                switch (nodename) {
//                    case "field":
//                        ModelField f = new ModelField();
//                        f.name = UtilXml.getAttributeIgnoreNull(entitychild, "name").toLowerCase();
//                        f.type = UtilXml.getAttributeIgnoreNull(entitychild, "type");
//                        f.defaultValue = UtilXml.getAttributeIgnoreNull(entitychild, "defaultValue");
//                        f.description = UtilXml.getAttributeIgnoreNull(entitychild, "description");
//                        f.title = UtilXml.getAttributeIgnoreNull(entitychild, "title");
//                        f.link = UtilXml.getAttributeIgnoreNull(entitychild, "link");
//                        if (UtilPub.isEmpty(f.title)) f.title = f.description;
//                        if (UtilPub.isEmptyStr(f.defaultValue)) {
//                            final ModelFieldType fieldType = f.getModelFieldType();
//                            if (fieldType != null) {
//                                f.defaultValue = fieldType.defaultValue;
//                            } else {
////                                UtilLogger.error("字段" + modelEntity.getEntityName() + "." + f.name + "类型错误" + f.type);
//                            }
//                        }
//                        modelEntity.addField(f);
//                        break;
//                    case "prim-key":
//                        //主键定义；
//                        String fieldname = UtilXml.getAttributeIgnoreNull(entitychild, "field").toLowerCase();
//                        final ModelField field = modelEntity.getField(fieldname);
//                        if (field == null) {
//                            System.out.println(MessageFormat.format("实体[{0}]主键定义的字段[{1}]在定义表中不存在!", modelEntity.getEntityName(), fieldname));
//                            continue;
//                        }
//                        field.isPk = true;
//                        field.defaultValue = null;
//                        modelEntity.getPks().add(field);
//                        break;
//                    case "index":
//                        //增加索引定义；
//                        ModelIndex modelIndex = new ModelIndex(modelEntity, entitychild);
//                        modelEntity.addIndex(modelIndex);
//                        break;
//                }
//            }
//            //迭代搜索抽象实体定义，多个用逗号分割，解析的时候解析为数组；
//            if (UtilPub.isNotEmpty(modelEntity.getExtendsEntityNames())) {
//                for (int i = 0; i < modelEntity.getExtendsEntityNames().length; i++) {
//                    String s = modelEntity.getExtendsEntityNames()[i];
//                    ModelEntity absEntity = abstractEntitydefs.get(s);
//                    if (absEntity == null) absEntity = entitydefs.get(s);
//                    merageEntityDef(modelEntity, absEntity);
//                }
//            }
//            if (modelEntity.getField("last_time") == null) {
//                ModelField f = new ModelField();
//                f.name = "last_time";
//                f.title = "最后更新时间";
//                f.type = "long";
//                f.defaultValue = "0";
//                f.description = "最后更新时间 yyyymmddhhMMss,如：20140218205532";
//                f.link = "";
//                modelEntity.addField(f);
//            }
//            //将实体定义字段放到主键，非主键(有可能主键在抽象实体中定义的)，本实体如果定义了主键，则不使用抽象实体的组件
//            modelEntity.updatePkLists();
//            if (this.entitydefs.get(modelEntity.getEntityName()) != null) {
//                String message = MessageFormat.format("实体定义 = {0}已经存在，不能重复定义!", modelEntity.getEntityName());
//                System.out.println(message);
//                throw new Exception(message);
//            }
//            if (modelEntity.getPks().size() == 0) {
//                String message = MessageFormat.format("实体定义 = {0}未定义主键!", modelEntity.getEntityName());
//                System.out.println(message);
//                throw new Exception(message);
//            }
//            this.entitydefs.put(modelEntity.getEntityName(), modelEntity);
//        }
//    }
//
//    //合并抽象实体到实际定义实体
//    private void merageEntityDef(ModelEntity entity, ModelEntity absentity) {
//        if (entity == null || absentity == null) return;
//        for (ModelField field : absentity.getFields()) {
//            entity.addField((ModelField)field.clone()); //合并实体定义；
//        }
////        for (ModelIndex absindex : absentity.getIndexes()) {
////            //entity.getIndexes().addAll(0,absentity.getIndexes()); //合并索引；
////            entity.addIndex(absindex.createIndexFor(entity));
////        }
//        //继续向上查找
//        if (absentity.isAbsEntity() && UtilPub.isNotEmpty(absentity.getExtendsEntityNames())) {
//            for (int i = 0; i < absentity.getExtendsEntityNames().length; i++) {
//                String s = absentity.getExtendsEntityNames()[i];
//                ModelEntity absEntity = abstractEntitydefs.get(s);
//                merageEntityDef(entity, absEntity);
//            }
//        }
//    }
//
//    public Map<String, ModelEntity> getEntitydefs() {
//        return entitydefs;
//    }
//
//    public ModelEntity getModelEntity(String entityName) {
//        return entitydefs.get(entityName);
//    }
//
//    public Set<String> getListSchema() {
//        return listSchema;
//    }
//}