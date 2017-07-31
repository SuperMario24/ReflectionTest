package com.example.saber.reflectiontest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

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
            Person person = (Person) cls.newInstance();//返回的是一个泛型对象
            person.setName("Sarr");
            person.setAge(18);
            Log.d(TAG,"person.toString():"+person.toString());



            Class<?> cls1 = Class.forName(Person.class.getName());
            Object object = (Object) cls1.newInstance();
            Method setName = cls1.getDeclaredMethod("setName",String.class);//获取setName()方法,第一个参数为方法名，第二个为该方法的参数类型
            setName.invoke(object,"Aimer");//设置调用setName的对象和传入setName的值
            Method setAge = cls1.getDeclaredMethod("setAge",int.class);
            setAge.invoke(object,18);
            Method[] methods = cls1.getDeclaredMethods();//获取类中所有的方法，无论是public还是private
            for(int i=0;i<methods.length;i++){
                if(methods[i].getName().equals("toString")){
                    Log.d(TAG,"Object.toString():"+methods[i].invoke(object));
                }
            }


            /**
             * 获取当前类所有构造方法修饰域和方法名称和参数类型
             */
            Constructor[] constructors = Class.forName(Person.class.getName()).getDeclaredConstructors();//返回该类中所有的构造函数数组（不分public和非public属性）
            for(int i=0;i<constructors.length;i++){

                int mod = constructors[i].getModifiers();//获取修饰域和方法名称
                Log.d(TAG, "Modifier.toString(mod):"+Modifier.toString(mod)+","+ Person.class.getName());//public, 包名.类名（方法名称）

                Class[] parameterTypes = constructors[i].getParameterTypes();//获取指定构造方法的参数的集合
                for(int j=0;j<parameterTypes.length;j++){
                    Log.d(TAG, "parameterTypes[j].getName():"+parameterTypes[j].getName());
                    if(parameterTypes.length > j+1){
                        Log.d(TAG, ",");
                    }
                }
            }

            /**
             * 获取当前类的所有方法
             *
             * 注意: 方法getDeclaredMethods()只能获取到由当前类定义的所有方法，不能获取从父类继承的方法
             * 方法getMethods() 不仅能获取到当前类定义的public方法，也能得到从父类继承和已经实现接口的public方法
             * 请查阅开发文档对这两个方法的详细描述。
             */
            Method[] methods1 = Class.forName(Person.class.getName()).getDeclaredMethods();
            for(int i=0;i<methods1.length;i++){
                int mod = methods1[i].getModifiers();//打印输出方法的修饰域
                Log.d(TAG, "getMethod---Modifier.toString(mod):"+Modifier.toString(mod));

                String returnType = methods1[i].getReturnType().getName();// 输出方法的返回类型
                Log.d(TAG, "methods1["+i+"].getReturnType().getName():"+methods1[i].getReturnType().getName());

                String methodName = methods1[i].getName();// 获取输出的方法名
                Log.d(TAG, "methods1["+i+"].getName():"+methods1[i].getName());

                Class[] parameterTypes = methods1[i].getParameterTypes();
                for(int j=0;j<parameterTypes.length;j++){
                    Log.d(TAG, "getMethod---parameterTypes[j].getName():"+parameterTypes[j].getName());
                    if(parameterTypes.length > j+1){
                        Log.d(TAG, ",");
                    }
                }

            }

            /**
             * 获取当前类的成员变量
             *
             * 注意: 对于未初始化的指针类型的属性，将不输出结果。
             */
            Field[] fields = Class.forName(Person.class.getName()).getDeclaredFields();
            for(int i=0;i<fields.length;i++){

                Class c = fields[i].getType();// 属性的类型
                int mod = fields[i].getModifiers();// 属性的修饰域
                Field field = Class.forName(Person.class.getName()).getDeclaredField(fields[i].getName());// 属性的值
                field.setAccessible(true); // 如果是 private 或者 package 权限的，一定要赋予其访问权限 Very Important
                Object value = field.get(Class.forName(Person.class.getName()).newInstance());

                if (value == null) {
                    Log.d(TAG,"getDeclaredFields():"+Modifier.toString(mod) + " " + c + " : "      + fields[i].getName());
                }
                else {
                    Log.d(TAG,"getDeclaredFields():"+Modifier.toString(mod) + " " + c + " : "  + fields[i].getName() + " = " + value.toString());
                }
            }


            /**
             * 修改私有字段的值
             */
            Person p1 = new Person();
            Log.d(TAG,"p1 origin age is "+ p1.getAge());

            Class<?> c = Class.forName(Person.class.getName());

            Field field = c.getDeclaredField("age");
            field.setAccessible(true);
            field.set(p1,15);//修改值
            Log.d(TAG,"after reflect age is "+ p1.getAge());



        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
