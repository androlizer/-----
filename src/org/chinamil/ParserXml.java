package org.chinamil;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.util.Xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
@SuppressWarnings("unused")
public class ParserXml<T> {
    

    public static String urlhead="http://wap.chinamil.com.cn/wap-paper/"; //地址
	/**
	 * 获取xml数据
	 * 
	 * @return
	 * @throws Exception
	 */
	public static LinkedList<PdfDomin> getPicture(String path) throws Exception {
		LinkedList<PdfDomin>  contacts = null;
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(3000);
		conn.setRequestMethod("GET") ; 
		if (conn.getResponseCode() == 200) {
			InputStream is = conn.getInputStream();
			contacts = parserPDFXml(is);
			is.close();
		} else {
			throw new RuntimeException();
		}
		return contacts;
	}

	/**
	 * xml 
	 * 测试的
	 *  @param is
	 * @return
	 * @throws XmlPullParserException 
	 * @throws IOException 
	 * @throws Exception
	 */
    public static LinkedList<PdfDomin> getPictureall(String path) throws Exception {
        LinkedList<PdfDomin>  contacts = null;
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET") ; 
        if (conn.getResponseCode() == 200) {
            InputStream is = conn.getInputStream();
            contacts = parserPDFXmlall(is);
            is.close();
        } else {
            throw new RuntimeException();
        }
        return contacts;
    }
    private static LinkedList<PdfDomin> parserPDFXml2(InputStream is) throws XmlPullParserException, IOException {
		LinkedList<PdfDomin> contacts = new LinkedList<PdfDomin>();
		PdfDomin video = null;
		XmlPullParser parser = Xml.newPullParser();
	
			parser.setInput(is, "UTF-8");
			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT&&contacts.size()<=7) {
				switch (eventType) {  
				case XmlPullParser.START_TAG:
					 if ("pdf".equals(parser.getName())) {
						video = new PdfDomin();
					} else if ("title".equals(parser.getName())) {
						video.title = parser.nextText();
					} else if ("cover".equals(parser.getName())) {
						video.cover = parser.nextText();
					} else if ("link".equals(parser.getName())) {
						video.link = parser.nextText();
					} else if ("channels".equals(parser.getName())) {
						video.channels = parser.nextText();
					}
					break;
				case XmlPullParser.END_TAG:
					if ("pdf".endsWith(parser.getName())) {
						contacts.add(video);
					}
					break;
				default:
					break;
				}
				eventType = parser.next();
			}
		
		return contacts;
	}
	/**
     * xml
     * 
     * @param is
     * @return
     * @throws XmlPullParserException
     * @throws IOException
     * @throws Exception
     * public String title;//标题
    public String cover;//缩略图地址
    public String link;//pdf连接地址
    public String channels;//说明
     */
    private static LinkedList<PdfDomin> parserPDFXml(InputStream is)
            throws XmlPullParserException, IOException {
        LinkedList<PdfDomin> contacts = new LinkedList<PdfDomin>();
        PdfDomin video = null;
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(is, "UTF-8");
        int eventType = parser.getEventType();
        boolean titleflag = false;
        boolean descriptionflag=false;
        while (eventType != XmlPullParser.END_DOCUMENT && contacts.size() < 7) {
            switch (eventType) {
            case XmlPullParser.START_TAG:
                if ("item".equals(parser.getName())) {
                    video = new PdfDomin();
                    titleflag = true;
                descriptionflag=true;
                    } else if ("title".equals(parser.getName())) {
                    if (titleflag) {
                        video.title = parser.nextText();
                        titleflag = false;
                    }
               }/* else if ("img".equals(parser.getName())) {
                 video.cover = urlhead+parser.getAttributeValue(0);
               } */else if ("content".equals(parser.getName())) {
                String contentString=parser.nextText();
                 video.link = urlhead+Char(contentString);
                 String img=CharImage(contentString);
                  video.cover = urlhead+CharImage(contentString);
                } else if ("description".equals(parser.getName())) {
                    if (descriptionflag) {
                        video.channels = parser.nextText();
                        descriptionflag=false;
                    }
                }
                break;
            case XmlPullParser.END_TAG:
                if ("item".endsWith(parser.getName())) {
                    contacts.add(video);
                }
                break;
            default:
                break;
            }
            eventType = parser.next();
        }
       /*   for (int i = 0; i < contacts.size(); i++) {
            PdfDomin odfDomin=contacts.get(i);
            System.out.println(odfDomin.channels+" "+odfDomin.title+"  " +
            		""+odfDomin.cover+" "+odfDomin.link);
        }
     System.out.println();
        System.out.println("集合的大小"+contacts.size());
      System.out.println(contacts);*/
        return contacts;
    }   
    private static LinkedList<PdfDomin> parserPDFXmlall(InputStream is)
            throws XmlPullParserException, IOException {
        LinkedList<PdfDomin> contacts = new LinkedList<PdfDomin>();
        PdfDomin video = null;
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(is, "UTF-8");
        int eventType = parser.getEventType();
        boolean titleflag = false;
        boolean descriptionflag=false;
        while (eventType != XmlPullParser.END_DOCUMENT && contacts.size() < 27) {
            switch (eventType) {
            case XmlPullParser.START_TAG:
                if ("item".equals(parser.getName())) {
                    video = new PdfDomin();
                    titleflag = true;
                descriptionflag=true;
                    } else if ("title".equals(parser.getName())) {
                    if (titleflag) {
                        video.title = parser.nextText();
                        titleflag = false;
                    }
               }/* else if ("img".equals(parser.getName())) {
                 video.cover = urlhead+parser.getAttributeValue(0);
               } */else if ("content".equals(parser.getName())) {
                String contentString=parser.nextText();
                 video.link = urlhead+Char(contentString);
                 String img=CharImage(contentString);
                  video.cover = urlhead+CharImage(contentString);
                } else if ("description".equals(parser.getName())) {
                    if (descriptionflag) {
                        video.channels = parser.nextText();
                        descriptionflag=false;
                    }
                }
                break;
            case XmlPullParser.END_TAG:
                if ("item".endsWith(parser.getName())) {
                    contacts.add(video);
                }
                break;
            default:
                break;
            }
            eventType = parser.next();
        }
       /*   for (int i = 0; i < contacts.size(); i++) {
            PdfDomin odfDomin=contacts.get(i);
            System.out.println(odfDomin.channels+" "+odfDomin.title+"  " +
                    ""+odfDomin.cover+" "+odfDomin.link);
        }
     System.out.println();
        System.out.println("集合的大小"+contacts.size());
      System.out.println(contacts);*/
        return contacts;
    }
public static String Char(String text) {
    int c=text.indexOf("ue=")+4;
    return text.substring(c,text.indexOf("\"",c));
}
	
public static String CharImage(String text) {
    int c=text.indexOf("src=")+5;
 int ss=  text.indexOf("\"",c);
   String sssString=text.substring(c,ss);
    return sssString;
            
}
	/**
	 * 获取图片，
	 * 如果这个图片存在就直接返回图片
	 * ,如果不存在就从服务上获取，
	 *  并且把获取的图片缓存起来 
	 */
	public static File getImage(String path, String title, File cache)
			throws Exception {
	 	String name = MD5.getMD5(title) + ".png";
		File file = new File(cache, name);
		if (file.exists()) {
			return file;
		} else {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("GET");
			if (conn.getResponseCode() == 200) {
				InputStream is = conn.getInputStream();
				FileOutputStream fos = new FileOutputStream(file);
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = is.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
				}
				is.close();
				fos.close();

				return file;
			}
		}
		return file;
	}



	/**
	 * 判斷網絡是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm == null) {
			return false;
		}
		NetworkInfo[] netinfo = cm.getAllNetworkInfo();
		if (netinfo == null) {
			return false;
		}
		for (int i = 0; i < netinfo.length; i++) {
			if (netinfo[i].isConnected()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 程序錯誤處理 主要針對網絡鏈接不可用的情況下
	 * 
	 * @param context
	 * @param what
	 */
	public static void ErrorDialog(Context context, String what) {
		AlertDialog.Builder buile = new AlertDialog.Builder(context);
		buile.setTitle(what);
		buile.setIcon(android.R.drawable.ic_dialog_alert);
		buile.setCancelable(false);
		buile.setItems(new CharSequence[] { "退出", "进入离线模式" },
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (which == 0) {
							System.exit(0);
						} else {
							// System.exit(0);
							// 进入离线模式 待处理 ;

						}
					}
				});

		buile.show();
	}

	/**
	 * 删除缓存目录
	 * 
	 * @param a
	 */
	public void del(File topcache) {

		File a[] = topcache.listFiles();
		if (topcache.exists()) {
			delent(a); // 先 不删除
			topcache.delete();

		}

	}

private void delent(File[] a) {
		for (int i = 0; i < a.length; i++) {
			Log.i("i", a[i] + "目录 ");
			if (a[i].isDirectory()) {
				delent(a[i].listFiles());
			}
			a[i].delete();

			// Log.i("i", aFile.getAbsolutePath()+"文件路径");
		}
	}

public static LinkedList<PdfDomin> getPictureallreal(String path) throws Exception {
    LinkedList<PdfDomin>  contacts = null;
    URL url = new URL(path);
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setConnectTimeout(5000);
    conn.setRequestMethod("GET") ; 
    if (conn.getResponseCode() == 200) {
        InputStream is = conn.getInputStream();
        contacts = parserPDFXmlallreal(is);
        is.close();
    } else {
        throw new RuntimeException();
    }
    return contacts;
}
private static LinkedList<PdfDomin> parserPDFXmlallreal(InputStream is)
        throws XmlPullParserException, IOException {
    LinkedList<PdfDomin> contacts = new LinkedList<PdfDomin>();
    PdfDomin video = null;
    XmlPullParser parser = Xml.newPullParser();
    parser.setInput(is, "UTF-8");
    int eventType = parser.getEventType();
    boolean titleflag = false;
    boolean descriptionflag=false;
    while (eventType != XmlPullParser.END_DOCUMENT) {
        switch (eventType) {
        case XmlPullParser.START_TAG:
            if ("item".equals(parser.getName())) {
                video = new PdfDomin();
                titleflag = true;
            descriptionflag=true;
                } else if ("title".equals(parser.getName())) {
                if (titleflag) {
                    video.title = parser.nextText();
                    titleflag = false;
                }
           }/* else if ("img".equals(parser.getName())) {
             video.cover = urlhead+parser.getAttributeValue(0);
           } */else if ("content".equals(parser.getName())) {
            String contentString=parser.nextText();
             video.link = urlhead+Char(contentString);
             String img=CharImage(contentString);
              video.cover = urlhead+CharImage(contentString);
            } else if ("description".equals(parser.getName())) {
                if (descriptionflag) {
                    video.channels = parser.nextText();
                    descriptionflag=false;
                }
            }
            break;
        case XmlPullParser.END_TAG:
            if ("item".endsWith(parser.getName())) {
                contacts.add(video);
            }
            break;
        default:
            break;
        }
        eventType = parser.next();
    }
   /*   for (int i = 0; i < contacts.size(); i++) {
        PdfDomin odfDomin=contacts.get(i);
        System.out.println(odfDomin.channels+" "+odfDomin.title+"  " +
                ""+odfDomin.cover+" "+odfDomin.link);
    }
 System.out.println();
    System.out.println("集合的大小"+contacts.size());
  System.out.println(contacts);*/
    return contacts;
}   
}
