using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using YamlDotNet.Serialization;

namespace CefSharp.utils
{
    class YamlSerializeObject
    {
        static public void Serializer<T>(T obj,string _filePath)            // 序列化操作  
        {
            StreamWriter yamlWriter = File.CreateText(_filePath);
            Serializer yamlSerializer = new Serializer();
            yamlSerializer.Serialize(yamlWriter, obj);
            yamlWriter.Close();
        }

        static public T Deserializer<T>(string _filePath)           // 泛型反序列化操作  
        {
            if (!File.Exists(_filePath))
            {
                throw new FileNotFoundException();
            }
            StreamReader yamlReader = File.OpenText(_filePath);
            Deserializer yamlDeserializer = new Deserializer();

            //读取持久化对象  
            T info = yamlDeserializer.Deserialize<T>(yamlReader);
            yamlReader.Close();
            return info;
        } 
    }
}
