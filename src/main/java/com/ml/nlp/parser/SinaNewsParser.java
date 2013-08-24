package com.ml.nlp.parser;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.beans.StringBean;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.HeadingTag;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.Span;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import com.ml.bus.model.News;

/**
 * 用于对搜狐网站上的新闻进行抓取
 */
public class SinaNewsParser extends AbstractNewsParser {
	
	/**
     * 获得新闻的标题
     * <h1 itemprop="headline">...<\/h1>
     */
	protected String getTitle(NodeFilter titleFilter, Parser parser) throws ParserException {
        String titleName = "";
        NodeList titleNodeList = (NodeList) parser.parse(titleFilter);
        for (int i = 0; i < titleNodeList.size(); i++) {
            HeadingTag title = (HeadingTag) titleNodeList.elementAt(i);
            titleName = title.getStringText();
        }
        return titleName;
    }

    /**
     * 获得新闻的责任编辑，也就是作者。
     * <div class="editer"><\/div>
     */
    protected String getAuthor(NodeFilter authorFilter, Parser parser) throws ParserException {
        String newsAuthor = "";
        NodeList authorList = (NodeList) parser.parse(authorFilter);
        for (int i = 0; i < authorList.size(); i++) {
            Div authorSpan = (Div) authorList.elementAt(i);
            newsAuthor = authorSpan.getStringText();
        }
        newsAuthor = newsAuthor.replaceAll("[(责任编辑：]", "");
        newsAuthor = newsAuthor.replaceAll("[)]", "");
        return newsAuthor;

    }

    /**
     * 获得新闻的日期
     * <div class="time" itemprop="datePublished" content="2013-08-07T15:07:00+08:00">2013年08月07日15:07<\/div>
     */
    protected String getDate(NodeFilter dateFilter, Parser parser) throws ParserException {
        String newsDate = "";
        NodeList dateList = (NodeList) parser.parse(dateFilter);
        for (int i = 0; i < dateList.size(); i++) {
        	Div dateTag = (Div) dateList.elementAt(i);
            newsDate = dateTag.getStringText();
        }
        return newsDate;
    }

    /**
     * 获取新闻的内容
     * <div itemprop="articleBody">...<\/div>
     */
    protected String getContent(NodeFilter contentFilter, Parser parser) throws ParserException {
        StringBuilder builder = new StringBuilder();
        NodeList contentList = (NodeList) parser.parse(contentFilter);
        for (int i = 0; i < contentList.size(); i++) {
            Div newsContenTag = (Div) contentList.elementAt(i);
            builder = builder.append(newsContenTag.getStringText());
        }
        
        String content = builder.toString();  //转换为String 类型。
        if (content != null) {
            parser.reset();
            parser = Parser.createParser(content, "gb2312");
            StringBean sb = new StringBean();
            sb.setCollapse(true);
            parser.visitAllNodesWith(sb);
            content = sb.getStrings();
            content = content.replaceAll("\\\".*[a-z].*\\}", "");

        } else {
           System.out.println("没有得到新闻内容！");
        }

        return content;
    }

    /**
     * 获取新闻的图片url
     * content body中的第一个图片，如果没有则为空
     */
	protected String getImg(NodeFilter imgFilter, Parser parser) throws ParserException {
		String url = "";
		
		StringBuilder builder = new StringBuilder();
        NodeList contentList = (NodeList) parser.parse(imgFilter);
        for (int i = 0; i < contentList.size(); i++) {
            Div newsContenTag = (Div) contentList.elementAt(i);
            builder = builder.append(newsContenTag.getStringText());
        }
        String content = builder.toString();  //转换为String 类型。
        if (content != null) {
            parser.reset();
            parser = Parser.createParser(content, "gb2312");
            NodeFilter filter = new TagNameFilter("img");
            NodeList imgList = parser.extractAllNodesThatMatch(filter);
            for (int i = 0; i < imgList.size(); i++) {
            	ImageTag imgNode = (ImageTag) imgList.elementAt(i);
                url = imgNode.getImageURL();
            }

        }
		return url;
	}
	
	/**
     * 获取新闻的来源
     * <span id="media_span" itemprop="publisher"><span itemprop="name">新华网<\/span><\/span>
     */
	protected String getSource(NodeFilter sourceFilter, Parser parser) throws ParserException {
		String source = "";
        NodeList sourceList = (NodeList) parser.parse(sourceFilter);
        for (int i = 0; i < sourceList.size(); i++) {
            Span sourceTag = (Span) sourceList.elementAt(i);
            Node node = sourceTag.childAt(0);
            source = node.toPlainTextString();
        }
        return source;
	}

	protected NodeFilters getNodeFilters() {
        NodeFilter titleFilter = new AndFilter(new TagNameFilter("h1"), new HasAttributeFilter("itemprop", "headline"));
        NodeFilter contentFilter = new AndFilter(new TagNameFilter("div"), new HasAttributeFilter("itemprop", "articleBody"));
        NodeFilter dateFilter = new AndFilter(new TagNameFilter("div"), new HasAttributeFilter("itemprop", "datePublished"));
        NodeFilter authorFilter = new AndFilter(new TagNameFilter("div"), new HasAttributeFilter("class", "editer"));
        NodeFilter imgFilter = new AndFilter(new TagNameFilter("div"), new HasAttributeFilter("itemprop", "articleBody"));
        NodeFilter sourceFilter = new AndFilter(new TagNameFilter("span"), new HasAttributeFilter("itemprop", "publisher"));
        
        NodeFilters nodeFilters = new NodeFilters(titleFilter, contentFilter, dateFilter, authorFilter, imgFilter, sourceFilter);
		return nodeFilters;
	}


    

    //单个文件测试网页
    public static void main(String[] args) throws ParserException {
        SinaNewsParser sina = new SinaNewsParser();
        News news = sina.parse("http://news.sohu.com/20130807/n383602425.shtml");
        System.out.println(news.toString());
    }

}
