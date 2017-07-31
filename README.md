# ReflectionTest

一.反射简介

JVM 提供了动态运行字节码的能力，除了 JVM 帮我们做链接、加载字节码相关的事情外，也通过反射提供给我们动态修改的能力。
反射使得我们能够在运行时，不知道类名、方法名的情况下查看其中的接口、方法和字段等信息，另一方面，也可以动态调用方法、新建对象，甚至篡改字段值。

总结起来就是，反射提供了一种与 Class 文件进行动态交互的机制。


二.Class类简介

在进行接下来的反射教程中，首先应该了解 Class Object。Java 中所有的类型，包括 int、float 等基本类型，都有与之相关的 Class 对象。
如果知道对应的 Class name，可以通过 Class.forName() 来构造相应的 Class 对象，如果没有对应的 class，或者没有加载进来，
那么会抛出 ClassNotFoundException 对象。

Class 封装了一个类所包含的信息，主要的接口如下:

      try {
        Class mClass = Class.forName("java.lang.Object");

        // 不包含包名前缀的名字
        String simpleName = mClass.getSimpleName();

        // 类型修饰符, private, protect, static etc.
        int modifiers = mClass.getModifiers();
        // Modifier 提供的一些用于判读类型的静态方法.
        Modifier.isPrivate(modifiers);

        // 父类的信息
        Class superclass = mClass.getSuperclass();

        // 构造函数
        Constructor[] constructors = mClass.getConstructors();

        // 字段类型
        Field[] fields = mClass.getFields();
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }


三.常用反射使用方法

1.调用方法：

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
            
            
            
            //Person.class.getName()获取完整的类名（包名.类名）
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

2.获取修改成员变量的值：

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



注：Class<?> cls=Class.forName("fanshe.Person");cls内部供我们使用的方法.

方法关键字	含义
getDeclareMethods()	获取所有的方法
getReturnType()	获取方法的返回值类型
getParameterTypes()	获取方法的传入参数类型
getDeclareMethod("方法名,参数类型.class,....")	获得特定的方法
-
构造方法关键字	含义
getDeclaredConstructors()	获取所有的构造方法
getDeclaredConstructors(参数类型.class,....)	获取特定的构造方法
-
成员变量	含义
getDeclaredFields	获取所有成员变量
getDeclaredField(参数类型.class,....)	获取特定的成员变量
-
父类和父接口	含义
getSuperclass()	获取某类的父类
getInterfaces()	获取某类实现的接口














