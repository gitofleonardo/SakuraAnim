package cn.huangchengxi.imomoe;

import org.jsoup.nodes.Document;
import org.junit.Test;

import cn.huangchengxi.imomoe.activities.main.MainModel;

public class MainModelTest {
    @Test
    public void testMainModel(){
        MainModel model=new MainModel();
        //String result=model.getHtmlText();
        //System.out.println(result);
        Document doc=model.getHtml();
        //System.out.println(doc.html());
        System.out.println(model.getRecommended(doc));
        System.out.println(model.getPageItems(doc,0));
        System.out.println(model.getPageItems(doc,1));
        System.out.println(model.getPageItems(doc,2));
        System.out.println(model.getPageItems(doc,3));
        System.out.println(model.getPageItems(doc,4));
    }
}
