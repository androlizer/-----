package org.chinamil;

import  org.chinamil.AndroidVersion;




public class Demo {
public int width;//屏幕宽
public int height;//屏幕高
public int bottomMargin;//下边距
public int  leftMargin;//左边距
public int  imagewidth;//缩略图 宽
public int  imageheight;//缩略图高
public int  headimageheight;//标题图片高
public int rowheight;//item 的高度
public int     version;

public Demo() {
}

@SuppressWarnings("unused")
private Demo(int width, int height, int bottomMargin, int leftMargin, int imagewidth, int imageheight,
        int headimageheight,int rowheight, int version) {
    super();
    this.width = width;
    this.height = height;
    this.bottomMargin = bottomMargin;
    this.leftMargin = leftMargin;
    this.imagewidth = imagewidth;
    this.imageheight = imageheight;
    this.headimageheight = headimageheight;
    this.version = version;
    this.rowheight=rowheight;
}




/**
 * 要屏幕的宽与高 60 60 
 *4 
 * 返回设和的数据
 * @return 
 */
public static  Demo getData(int screenWidth,int screenHeight){
    System.out.println(screenWidth+"屏幕前宽后高"+screenHeight);
   Demo demo=new Demo();
   demo.width=screenWidth;
   demo.height=screenHeight;
   if (AndroidVersion.VERSION>=14) {
       System.out.println("版本为你14以上");
       switch (screenWidth) {
           case 800:  //800 1280
               System.out.println("进来800的");
            demo.rowheight=screenHeight-110;
               demo.imagewidth=(screenWidth-200)/3;
               demo.imageheight=(screenHeight-340)/3;
                  demo.leftMargin=50;
                       demo.bottomMargin=30;
                demo.headimageheight=110;
              break;
           case 720:  
               System.out.println("进来720的");
            demo.rowheight=screenHeight-110;
               demo.imagewidth=(screenWidth-200)/3;
               demo.imageheight=(screenHeight-450)/3;
                  demo.leftMargin=50;
                       demo.bottomMargin=30;
                demo.headimageheight=110;
              break;
              
         case 600:
             demo.rowheight=screenHeight-110;
             System.out.println("进来600的");
             demo.imagewidth=(screenWidth-180)/3;
             demo.imageheight=(screenHeight-350)/3;
             demo.leftMargin=45;
             demo.bottomMargin=20;
             demo.headimageheight=110;
           break;
         case 768:
             System.out.println("进来768的");
              demo.rowheight=screenHeight-110;
             demo.imagewidth=(screenWidth-300)/3;
             demo.imageheight=(screenHeight-380)/3;
                demo.leftMargin=70;
                demo.bottomMargin=27;
          demo.headimageheight=110;
             break;
        case 640:
            demo.rowheight=screenHeight-110;
            System.out.println("进来640的");
            demo.imagewidth=(screenWidth-220)/3;
            demo.imageheight=(screenHeight-350)/3;
            demo.leftMargin=50;///////////////
            demo.bottomMargin=20;
            demo.headimageheight=110;
             break;
             
        case 480:
    if (screenHeight==640) {  // 640
        demo.imageheight=(screenHeight-180)/3;
        demo.rowheight=screenHeight-60;
        demo.leftMargin=40;
        System.out.println("进来480 的  640 ");
           demo.bottomMargin=10;
           demo.headimageheight=60;
            }else if(screenHeight==800) {
                demo.imageheight=(screenHeight-280)/3;
                demo.rowheight=screenHeight-80;
                demo.leftMargin=40;
                System.out.println("进来480 的  800 ");
                   demo.bottomMargin=15;
                   demo.headimageheight=80;
                   
            }else {  //854  
                demo.imageheight=(screenHeight-300)/3;
                demo.rowheight=screenHeight-80;
                demo.leftMargin=40;
                System.out.println("进来480 的  800 ");
                   demo.bottomMargin=20;
                   demo.headimageheight=80;
            }
     demo.imagewidth=(screenWidth-160)/3;
      break;
        case 320:
            //等会再测试这个
            demo.rowheight=screenHeight-50;
            System.out.println("进来320的");
            demo.imagewidth=(screenWidth-100)/3;
            demo.imageheight=(screenHeight-160)/3;
            demo.leftMargin=25;
            demo.bottomMargin=10;
            demo.headimageheight=50;
            break;
        default :
            def(screenWidth,screenHeight,demo);
        break;
      }
}else {
 //800×480
    System.out.println("版本为你14以下 ");
  switch (screenWidth) {
       case 800:  //800 1280
           System.out.println("进来800的");
        demo.rowheight=screenHeight-110;
           demo.imagewidth=(screenWidth-340)/3;
           demo.imageheight=(screenHeight-500)/3;
              demo.leftMargin=85;
                   demo.bottomMargin=40;
            demo.headimageheight=110;
          break;
     case 600:
       demo.headimageheight=110;
        demo.rowheight=screenHeight-110;
         System.out.println("进来600的");
demo.imagewidth=(screenWidth-230)/3;//230
    demo.imageheight=(screenHeight-430)/3;
     demo.leftMargin=60;//60
 demo.bottomMargin=30;
       break;
     case 768://guo
         System.out.println("进来768的");
         demo.rowheight=screenHeight-110;
            if (screenHeight==1024) {
                demo.imagewidth=(screenWidth-280)/3;
                demo.imageheight=(screenHeight-400)/3;//300
                demo.leftMargin=70;
                demo.bottomMargin=33;
               System.out.println("进来768的1024");
            }else {
                demo.imagewidth=(screenWidth-300)/3;//270
                demo.imageheight=(screenHeight-350)/3;
                demo.leftMargin=75;//70
                demo.bottomMargin=27;
                System.out.println("进来768 非 1024");
                  }
         
         demo.headimageheight=110;
         break;
    case 640:
        demo.rowheight=screenHeight-110;
        System.out.println("进来640的");
        demo.imagewidth=(screenWidth-230)/3;
        demo.imageheight=(screenHeight-350)/3;
        demo.leftMargin=60;
        demo.bottomMargin=30;
        demo.headimageheight=110;
         break;
    case 480:
if (screenHeight==640) {  // 640
    demo.imageheight=(screenHeight-200)/3;
    demo.imagewidth=(screenWidth-220)/3;
    demo.rowheight=screenHeight-60;
    demo.leftMargin=50;
    System.out.println("进来480 的  640 ");
       demo.bottomMargin=20;
       demo.headimageheight=60;
        }else if(screenHeight==800) {  //80
            demo.imageheight=(screenHeight-370)/3;
            demo.imagewidth=(screenWidth-160)/3;
            demo.rowheight=screenHeight-80;
            demo.leftMargin=40;
            System.out.println("进来480 的  800 ");
               demo.bottomMargin=30;
               demo.headimageheight=80;
        }else {  //854  
            demo.imageheight=(screenHeight-320)/3;
            demo.imagewidth=(screenWidth-160)/3;
            demo.rowheight=screenHeight-80;
            demo.leftMargin=40;
            System.out.println("进来480 的  850 ");
               demo.bottomMargin=30;
               demo.headimageheight=80;
        }
  break;
    case 320:
        //等会再测试这个    日期过高
        demo.rowheight=screenHeight-50;
        System.out.println("进来600的");
        demo.imagewidth=(screenWidth-100)/3;
        demo.imageheight=(screenHeight-160)/3;
        demo.leftMargin=25;
        demo.bottomMargin=13;
        demo.headimageheight=50;
        break;
    default :
    break;
  }
  }

  
  return demo;
    
}
public static void def(int  screenWidth,int screenHeight,Demo demo) {
    if (screenWidth < 480) {
        demo.imagewidth = (screenWidth - 85) / 3;
        demo.imageheight = (screenHeight - 50) / 3;
        demo.headimageheight = 50;
        demo.rowheight=screenHeight-50;
        demo.bottomMargin=15;
        demo.leftMargin=25; 
     } else {
         demo.imagewidth  = (screenWidth - 125) / 3;
         demo.imageheight = (screenHeight - 110) / 3;
         demo.headimageheight = 110;
        demo.rowheight=screenHeight-110;
        demo.bottomMargin=25;
        demo.leftMargin=35;
 }
}
/*LayoutParams wangqi=manager.getLayoutParams();
wangqi.height=4;
wangqi.width=53;
manager.setLayoutParams(wangqi);
} else {
imageWidth = (screenWidth - 125) / 3;
// 70
imageHeight = (screenHeight - 110) / 3;
headimageheight = 110;
}
*/

}
