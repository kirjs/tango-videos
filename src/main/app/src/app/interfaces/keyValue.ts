export interface KeyValue {
    key:String;
    value: String;
}

export class KeyValueUtils {
    static toKeyValue(key:String, value:String):KeyValue {
        return {
            key: key,
            value: value
        };
    }

    static valueToKeyValue(value:String):KeyValue {
        return KeyValueUtils.toKeyValue(value, value);
    }


    static valueObjectToKeyValue(object:any):KeyValue {
        return KeyValueUtils.valueToKeyValue(object.value);
    }
}
