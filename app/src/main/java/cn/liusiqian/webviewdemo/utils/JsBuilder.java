package cn.liusiqian.webviewdemo.utils;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Créé par liusiqian 2017/6/20.
 */

public class JsBuilder
{
    //Constants
    private static final String HEAD = "javascript:";
    private static final String DEFAULT_OBJECT = "window.android";
    private static final String TAIL = "void(0);";
    
    //Parameters
    private List<ParamPair> paramPairs;
    private String object;
    private String method;
    
    public JsBuilder() {
        object = DEFAULT_OBJECT;
    }
    
    public JsBuilder(@NonNull String object){
        this.object = object;
    }
    
    public JsBuilder setMethod(@NonNull String method) {
        this.method = method;
        return this;
    }
    
    public <V> JsBuilder addParam(@NonNull V value, @NonNull Class<V> cls){
        if(Byte.class == cls || byte.class == cls){
            //不支持byte
            throw new IllegalArgumentException("invalid parameter type : byte");
        }
        
        ParamPair<V> pair = new ParamPair<>(value, cls);
        if(paramPairs == null){
            paramPairs = new ArrayList<>();
        }
        paramPairs.add(pair);
        return this;
    }
    
    public String build(){
        if(TextUtils.isEmpty(method)){
            throw new IllegalStateException("js method undefined!");
        }
        
        StringBuilder sb = new StringBuilder(HEAD);
        sb.append(object).append(" && ").append(object).append(".").append(method)
                .append("(");
        
        //去掉最后一个逗号
        if(paramPairs != null && paramPairs.size() > 0){
            for (ParamPair pair : paramPairs){
        
                if( typeofNumberOrBoolean(pair.type) ){
                    //基本类型直接添加字符串
                    sb.append(pair.value).append(",");
                }
                else if(String.class == pair.type){
                    if(typeofNullOrUndefined((String) pair.value)){
                        //null 和 undefined 也是直接添加
                        sb.append(pair.value).append(",");
                    }
                    else {
                        //String 类型增加转义符
                        sb.append("\"").append(pair.value).append("\",");
                    }
                }
                else if(char.class == pair.type || Character.class == pair.type){
                    //Char处理方式与String相同
                    sb.append("\"").append(pair.value).append("\",");
                }
                else{
                    //Object只能调用toString了
                    sb.append("\"").append(pair.value.toString()).append("\",");
                }
            }
            
            sb.deleteCharAt(sb.lastIndexOf(","));
        }
        sb.append(");").append(TAIL);
        
        return sb.toString();
    }
    
    private boolean typeofNumberOrBoolean(Class cls){
        return cls == int.class  || cls == Integer.class ||
            cls == double.class  || cls == Double.class  ||
            cls == float.class   || cls == Float.class   ||
            cls == boolean.class || cls == Boolean.class ||
            cls == short.class   || cls == Short.class   ||
            cls == long.class    || cls == Long.class;
    }
    
    private boolean typeofNullOrUndefined(String param){
        return "null".equals(param) || "undefined".equals(param);
    }
    
    private class ParamPair<T> {
        T value;
        Class<T> type;
        
        ParamPair(T value,Class<T> type) {
            this.value = value;
            this.type = type;
        }
    }
}
