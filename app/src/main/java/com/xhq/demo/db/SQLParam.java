package com.xhq.demo.db;

//一个sql及其参数
public class SQLParam{
    public String sql = "";
    public Object[] paras = null;
    public int[] types = null;
    public boolean hasLob = false; //是否有lob字段

    public SQLParam(String sql, Object... paras) {
        this.sql = sql;
        this.paras = paras;
    }

    public SQLParam(String sql, int[] types, Object... paras) {
        this.sql = sql;
        this.paras = paras;
        this.types = types;
    }

    public SQLParam(String sql, int[] types, boolean hasLob, Object... paras) {
        this.sql = sql;
        this.paras = paras;
        this.types = types;
        this.hasLob = hasLob;
    }

    @Override
    public int hashCode() {
        //计算Hash值，以判断条件参数是否有变更；
        if (paras == null) return sql.hashCode();
        int result = 1;
        for (Object element : paras) {
            result = 31 * result + (element == null ? 0: element.hashCode());
        }
        result = 31 * result + sql.hashCode();
        return result;
    }

    public String getSqlNoOrderBy() {
        String sSql = sql.toUpperCase();
        int n = sSql.lastIndexOf("ORDER BY ");
        if (n < 0) return sql;
        String retSql = sql.substring(0, n);
        String s = sSql.substring(n);
        if (s.contains("SELECT")) return sql;
        if (!s.contains(")")) return retSql;
        n = 0;
        for (int i = 0, len = s.length(); i < len; i++) {
            final char c = s.charAt(i);
            if (c == '(') n++;
            else if (c == ')') n--;
            if (n < 0) return sql;
        }
        return retSql;
    }
}
