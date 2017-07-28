package com.example.saber.reflectiontest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final String packageName = "com.example.saber.reflectiontest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            //new 方式创建的代码
            Person p = new Person();
            p.setName("Sarr");
            p.setAge(18);


            /**
             * 反射机制实现
             * 调用newInstance()方法在让Class对象在内存中创建对应的实例,并且让person引用实例的内存地址
             *
             * 注意:
             * cls.newInstance()方法返回的是一个泛型T,我们要强转成Person类
             * cls.newInstance()默认返回的是Person类的无参数构造对象
             * 被反射机制加载的类必须有无参数构造方法,否者运行会抛出异常
             */
            Class<?> cls = Class.forName(packageName+".Person");//forName("包名.类名")
            Person person = (Person) cls.newInstance();
            person.setName("Sarr");
            person.setAge(18);
            Log.d(TAG,"person.toString():"+person.toString());



            Class<?> cls1 = Class.forName(Person.class.getName());
            Object object = (Object) cls1.newInstance();
            Method setName = cls1.getDeclaredMethod("setName",String.class);//获取setName()方法,第一个参数为方法名，第二个为该方法的参数类型
            setName.invoke(object,"Aimer");//设置调用setName的对象和传入setName的值
            Method setAge = cls1.getDeclaredMethod("setAge",int.class);
            setAge.invoke(object,18);
            Method[] methods = cls1.getDeclaredMethods();
            for(int i=0;i<methods.length;i++){
                if(methods[i].getName().equals("toString")){
                    Log.d(TAG,"Object.toString():"+methods[i].invoke(object));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
