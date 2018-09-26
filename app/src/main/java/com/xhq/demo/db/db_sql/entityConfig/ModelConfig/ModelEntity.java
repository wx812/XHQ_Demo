package com.xhq.demo.db.db_sql.entityConfig.ModelConfig;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 一个实体的相关定义；
 */
@SuppressWarnings({"UnusedDeclaration", "NullableProblems"})
public class ModelEntity {
    private String entityName = "";//The entity-name of the Entity (抽象实体就是抽象实体名)
    //private String extendsEntityName = "";
    private String title = "";//标题，汉字名
    private int version = 0;//版本

    private List<ModelField> fields = new ArrayList<>(); //字段
    private Map<String, ModelField> fieldsMap = new HashMap<>();

    private ModelField pkField = null; //主键    --- 所有实体都使用单主键
    private List<ModelIndex> indexes = new ArrayList<>(); //索引

    public String getEntityName() {
        return entityName;
    }

    public ModelEntity(String name, String title, int version) {
        this.entityName = name;
        this.title = title;
        this.version = version;
    }

    public void addPkField(String name) {
        this.pkField = addStrField(name, "ID", 32);
    }

    //增加整型字段
    public ModelField addIntField(String name, String title) {
        return addIntField(name, title, "0");
    }
    public ModelField addIntField(String name, String title, String defaultValue) {
        return addField(name, title, ModelConsts.FieldType.INT.value, 0, 0, defaultValue);
    }

    //增加浮点型字段
    public ModelField addDoubleField(String name, String title, int length, int precision) {
        return addDoubleField(name, title, length, precision, "0");
    }
    public ModelField addDoubleField(String name, String title, int length, int precision, String defaultValue) {
        return addField(name, title, ModelConsts.FieldType.NUMERIC.value, length, precision, defaultValue);
    }

    //增加字符串字段
    public ModelField addStrField(String name, String title, int length) {
        return addStrField(name, title, length, null);
    }
    public ModelField addStrField(String name, String title, int length, String defaultValue) {
        return addField(name, title, ModelConsts.FieldType.CHAR.value, length, 0, defaultValue);
    }

    //加索引
    public void addIndex(String indexName, String... fields) {
        List<String> list = new ArrayList<>();
        Collections.addAll(list, fields);
        ModelIndex modelIndex = new ModelIndex(this);
        modelIndex.setName(indexName);
        modelIndex.fieldNames = list;
        this.indexes.add(modelIndex);
    }

    private ModelField addField(String name, String title, int type, int length, int precision, String defaultValue) {
        ModelField modelField = new ModelField();
        modelField.name = name;
        modelField.precision = precision;
        modelField.title = title;
        modelField.type = type;
        modelField.length = length;
        modelField.defaultValue = defaultValue;
        fields.add(modelField);
        fieldsMap.put(name, modelField);
        return modelField;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public List<ModelField> getFields() {
        return fields;
    }

    public Map<String, ModelField> getFieldsMap() {
        return fieldsMap;
    }

    public ModelField getPkField() {
        return pkField;
    }

    public List<ModelIndex> getIndexes() {
        return indexes;
    }

    public ModelField getField(String fieldName) {
        if (fieldName == null) return null;
        if (fieldsMap == null) {
            createFieldsMap();
        }
        return fieldsMap.get(fieldName);
    }

    private synchronized void createFieldsMap() {
        Map<String, ModelField> tempMap = new HashMap<>(fields.size());
        for (ModelField field : fields) {
            tempMap.put(field.name, field);
        }
        fieldsMap = tempMap;
    }

    public void addField(ModelField field) {
        if (field == null) return;
        if (fieldsMap == null) {
            createFieldsMap();
        }
        if (fieldsMap.containsKey(field.name)) return;
        this.fields.add(field);
        this.fieldsMap.put(field.name, field);
    }

    public List<String> getAllFieldNames() {
        return getFieldNamesFromFieldVector(fields);
    }

    public String getPkFieldName() {
        return pkField.name;
    }

    public List<String> getNoPkFieldNames() {
        List<String> nameList = new ArrayList<>(fields.size());
        if (fields.size() <= 0)
            return nameList;
        for (ModelField field : fields) {
            if(field != pkField){
                nameList.add(field.name);
            }
        }
        return nameList;
    }

    public List<ModelField> getNoPkFields() {
        List<ModelField> nameList = new ArrayList<>(fields);
        nameList.remove(pkField);
        return nameList;
    }

    private List<String> getFieldNamesFromFieldVector(List<ModelField> modelFields) {
        List<String> nameList = new ArrayList<>(modelFields.size());
        if (modelFields.size() <= 0)
            return nameList;
        for (ModelField field : modelFields) {
            nameList.add(field.name);
        }
        return nameList;
    }


    public Iterator<ModelIndex> getIndexesIterator() {
        return this.indexes.iterator();
    }

    public String nameString(List<ModelField> flds, String separator, String afterLast) {
        return nameString(flds, "", separator, afterLast);
    }

    /**
     * 字段名称使用指定分隔方式连接；
     *
     * @param flds      字段列表
     * @param separator 分割符号
     * @param afterLast 收尾字符；
     * @return String;
     */
    public String nameString(List<ModelField> flds, String fieldPrex, String separator, String afterLast) {
        StringBuilder sb = new StringBuilder();
        if (flds.size() < 1) {
            return "";
        }
        int i = 0;
        for (; i < flds.size() - 1; i++) {
            sb.append(fieldPrex).append(flds.get(i).name);
            sb.append(separator);
        }
        sb.append(fieldPrex).append(flds.get(i).name);
        sb.append(afterLast);
        return sb.toString();
    }


    /**
     * 字段名称使用指定分隔方式连接；
     *
     * @param flds      字段列表
     * @param separator 分割符号
//     * @param afterLast 收尾字符；
     * @return String;
     */
    public String nameString(String flds, String fieldPrex , String separator) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        sb.append(fieldPrex).append(flds);
        sb.append(separator);
        return sb.toString();
    }



    //类型 + 字段名，采用逗号分割，形成字符串；
    public String typeNameString(List<ModelField> flds) {
        StringBuilder sb = new StringBuilder();
        if (flds.size() < 1) {
            return "";
        }
        int i = 0;
        for (; i < flds.size() - 1; i++) {
            ModelField curField = flds.get(i);
            sb.append(curField.name);
            sb.append(" ");
            sb.append(ModelConsts.FieldType.getSqlTypeByValue(curField.type));
            sb.append(", ");
        }
        ModelField curField = flds.get(i);
        sb.append(curField.name);
        sb.append(" ");
        sb.append(ModelConsts.FieldType.getSqlTypeByValue(curField.type));
        return sb.toString();
    }

    public String colNameString(List<ModelField> flds) {
        return nameString(flds, ", ", "");
    }

    public int getFieldsSize() {
        return this.fields.size();
    }
}
