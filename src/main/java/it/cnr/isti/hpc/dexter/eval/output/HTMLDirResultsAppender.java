/**
 *  Copyright 2014 Diego Ceccarelli
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package it.cnr.isti.hpc.dexter.eval.output;

import it.cnr.isti.hpc.dexter.eval.AnnotatedSpot;
import it.cnr.isti.hpc.dexter.eval.cmp.AnnotatedSpotComparator;
import it.cnr.isti.hpc.dexter.eval.collector.MetricValuesCollector;
import it.cnr.isti.hpc.dexter.eval.metrics.MetricUtil;
import it.cnr.isti.hpc.dexter.eval.reader.CharacterOffsetToByteOffsetCalculator;
import it.cnr.isti.hpc.io.IOUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.jatl.Html;

/**
 * Prints the results on the std out.
 * 
 * @author Diego Ceccarelli <diego.ceccarelli@isti.cnr.it>
 * 
 *         Created on Feb 17, 2014
 */
public class HTMLDirResultsAppender implements OutputResultsAppender {

	boolean partial = true;
	MetricUtil utils = new MetricUtil();
	Html html = null;

	private int id = 0;
	private final File dir;

	List<MetricValuesCollector<?>> collectors = new LinkedList<MetricValuesCollector<?>>();

	public HTMLDirResultsAppender(String f, AnnotatedSpotComparator comparator) {
		dir = new File(f);
		if (!dir.exists())
			dir.mkdir();
	}

	private static final Logger logger = LoggerFactory
			.getLogger(HTMLDirResultsAppender.class);

	public void append(MetricValuesCollector<?> metric) {
		double score = metric.getScore();
		html.div().strong().text(metric.getName()).text(":").end();
		html.pre().text(String.valueOf(score)).end().end();
	}

	public boolean isAppendPartial() {
		return partial;
	}

	public void appendPartial(List<AnnotatedSpot> predictions,
			List<AnnotatedSpot> goldenTruth, AnnotatedSpotComparator comparator) {

	}

	public void appendPartial(String text, List<AnnotatedSpot> predictions,
			List<AnnotatedSpot> goldenTruth, AnnotatedSpotComparator comparator) {
		if (goldenTruth.isEmpty()) {
			logger.warn("empty golden truth!");
		} else {
			int len = text.length();
			try {
				int bytelen = text.getBytes("UTF-8").length;
				if (len != bytelen) {
					System.out.println(goldenTruth.get(0).getDocId() + " "
							+ (bytelen - len));
				}
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			File file = new File(dir, id + ".html");
			List<AnnotatedSpot> tpPredictions = new ArrayList<AnnotatedSpot>();
			List<AnnotatedSpot> tpGoldenTruth = new ArrayList<AnnotatedSpot>();
			List<AnnotatedSpot> fp = new ArrayList<AnnotatedSpot>();
			List<AnnotatedSpot> fn = new ArrayList<AnnotatedSpot>();

			utils.intersect(predictions, goldenTruth, tpPredictions,
					tpGoldenTruth, fp, fn, comparator);
			BufferedWriter bw = IOUtils.getPlainOrCompressedUTF8Writer(file
					.getAbsolutePath());
			html = new Html(bw);
			html.html();
			html.head();
			html.title("Dexter Eval");
			html.style()
					.text(".tp {			background-color:#66FF99;}.fp {			background-color:#FF9966;		}		.fn {			background-color:#ffff00;		}");
			html.end();
			html.link()
					.href("http://getbootstrap.com/dist/css/bootstrap.min.css")
					.media("screen").rel("stylesheet").type("text/css");
			html.link()
					.href("http://mottie.github.io/tablesorter/css/theme.blue.css")
					.media("screen").rel("stylesheet").type("text/css");

			html.script()
					.src("http://mottie.github.io/tablesorter/docs/js/jquery-latest.min.js")
					.end();
			html.script()
					.src("http://mottie.github.io/tablesorter/js/jquery.tablesorter.js")
					.end();

			html.script()
					.type("text/javascript")
					.text("\n\n" + "$(function() {\n"
							+ "$('.tp').tablesorter({ \n" + "theme : 'blue'\n"
							+ "});\n" + "$('.fn').tablesorter({ \n"
							+ "theme : 'blue'\n" + "});\n"
							+ "$('.fp').tablesorter({ \n" + "theme : 'blue'\n"
							+ "});\n" + "$('.main').tablesorter({ \n"
							+ "theme : 'blue'\n" + "});" + "});\n").end();

			html.end();
			html.body();
			html.div().classAttr("container");
			html.div().style().text("text-align:right").end();
			if (id > 0) {
				html.a().href((id - 1) + ".html").text("[Prev]").end();
			} else {
				html.a().href("#").text("[Prev]").end();
			}
			html.a().href((id + 1) + ".html").text("[Next]").end();
			html.end();

			html.h1()
					.text("Dexter Eval [" + goldenTruth.get(0).getDocId() + "]")
					.end();
			html.strong().text("Comparator: ").end();
			html.p()
					.text(comparator.getName() + ": "
							+ comparator.getDescription()).end();

			html.div().classAttr("text");

			html.span().classAttr("tp").text("true positives").end();
			html.text(" - ");
			html.span().classAttr("fp").text("false positives").end();
			html.text(" - ");
			html.span().classAttr("fn").text("false negatives").end();
			html.br();
			html.br();

			List<AnnotatedSpot> all = new ArrayList<AnnotatedSpot>();
			all.addAll(tpPredictions);
			all.addAll(fp);
			all.addAll(fn);
			Collections.sort(all, new AnnotatedSpot.SortByStart());

			CharacterOffsetToByteOffsetCalculator calc = new CharacterOffsetToByteOffsetCalculator();
			calc.loadString(text);

			for (AnnotatedSpot as : all) {
				as.setStart(calc.getCharacterOffset(as.getStart()));
				as.setEnd(calc.getCharacterOffset(as.getEnd()));
			}

			int last = 0;
			for (AnnotatedSpot as : all) {
				if (as.getStart() < 0 || as.getStart() >= text.length()
						|| as.getEnd() < 0 || as.getEnd() > text.length()
						|| last < 0 || last > text.length()) {
					logger.error(
							"current spot {} is out of bound in text:\n\n {}\n\n ",
							as, text);
					continue;
				}
				if (last > as.getStart()) {
					// logger.error("overlapping spot {} just adding", as);

				} else {

					html.raw(eval(text.substring(last, as.getStart())));
				}
				html.span();
				if (tpPredictions.contains(as))
					html.classAttr("tp");
				if (fp.contains(as))
					html.classAttr("fp");
				if (fn.contains(as))
					html.classAttr("fn");
				if (as.getEnd() < text.length()) {
					html.strong()
							.raw(eval(text.substring(as.getStart(),
									as.getEnd() + 1))).end();
				} else {
					html.strong()
							.raw(eval(text.substring(as.getStart(), as.getEnd())))
							.end();
				}

				html.small().tt()
						.text("[" + as.getSpot() + "|" + as.getEntity() + "]");
				if (tpPredictions.contains(as)) {
					int i = tpPredictions.indexOf(as);
					AnnotatedSpot golden = tpGoldenTruth.get(i);
					html.text(" gt[" + golden.getSpot() + "|"
							+ golden.getEntity() + "]");

				}
				html.end().end().end();
				last = as.getEnd();
			}
			html.raw(eval(text.substring(last, text.length())));

			html.end();

			html.div().classAttr("row").id(goldenTruth.get(0).getDocId()).h3()
					.text(goldenTruth.get(0).getDocId());

			html.div().classAttr("sum main");
			html.table().classAttr("main");
			html.thead();
			html.tr();
			for (MetricValuesCollector<?> collector : collectors) {

				html.th().text(collector.getName()).end();

			}
			html.end();
			html.end();
			html.tbody();
			html.tr();
			for (MetricValuesCollector<?> collector : collectors) {
				html.td();
				Object o = collector.getPartial();
				double f = 0;
				if (o instanceof Integer) {
					int i = ((Integer) o).intValue();
					html.text(String.format("%d", i)).end();
					continue;
				}

				if (o instanceof Float) {
					f = (Float) (o);
				}
				if (o instanceof Double) {
					f = (Double) o;
				}
				html.text(String.format("%.3f", f)).end();

			}
			html.end();
			html.end();
			html.end();

			html.div().id(goldenTruth.get(0).getDocId() + "-detail")
					.classAttr("detail");
			html.div().classAttr("tp  eval ");
			html.h3().text("True Positives").end();
			html.table().classAttr("tp table table-striped");
			html.thead();
			html.tr();
			html.th().text("Id").end();
			html.th().text("Golden Truth mention").end();
			html.th().text("Tagger mention").end();

			html.th().text("Golden Truth start").end();
			html.th().text("Tagger start").end();

			html.th().text("Golden Truth end").end();
			html.th().text("Tagger end").end();

			html.th().text("Golden Truth entity").end();
			html.th().text("Tagger entity").end();

			html.th().text("Golden Truth name").end();

			html.th().text("Tagger name").end();
			html.th().text("Tagger condidence").end();
			html.end();
			html.end();
			html.tbody();
			for (int i = 0; i < tpGoldenTruth.size(); i++) {
				html.tr();
				html.td().text(String.valueOf(i + 1)).end();
				AnnotatedSpot s = tpGoldenTruth.get(i);
				AnnotatedSpot t = tpPredictions.get(i);
				html.td().strong().text(cleanMention(s.getSpot())).end().end();
				html.td().text(cleanMention(t.getSpot())).end();

				html.td().strong().text("" + s.getStart()).end().end();
				html.td().text("" + t.getStart()).end();

				html.td().strong().text("" + s.getEnd()).end().end();
				html.td().text("" + t.getEnd()).end();

				html.td().strong().text(s.getEntity()).end().end();
				html.td().text(t.getEntity()).end();

				html.td().strong().text(s.getWikiname()).end().end();

				html.td().text(t.getWikiname()).end();
				html.td().text(String.format("%.3f", t.getConfidenceScore()))
						.end();

				html.end();
			}
			html.end();
			html.end();
			html.end();
			html.div().classAttr("fn  eval");

			html.h3().text("False Negatives").end();
			html.table().classAttr("fn table table-striped");
			html.thead();
			html.tr();
			html.th().text("Id").end();
			html.th().text("Golden Truth mention").end();

			html.th().text("Golden Truth start").end();

			html.th().text("Golden Truth end").end();

			html.th().text("Golden Truth entity").end();

			html.th().text("Golden Truth name").end();
			// html.th().text("Condidence").end();
			html.end();
			html.end();
			html.tbody();
			for (int i = 0; i < fn.size(); i++) {
				html.tr();
				html.td().text(String.valueOf(i + 1)).end();

				AnnotatedSpot t = fn.get(i);
				html.td().text(cleanMention(t.getSpot())).end();

				html.td().text("" + t.getStart()).end();

				html.td().text("" + t.getEnd()).end();

				html.td().text(t.getEntity()).end();

				html.td().text(t.getWikiname()).end();
				// html.td().text(String.format("%.3f",
				// t.getConfidenceScore())).end();

				html.end();

			}
			html.end();
			html.end();
			html.end();

			html.div().classAttr("fp  eval");
			html.h3().text("False Positives").end();

			html.table().classAttr("fp table table-striped");
			html.thead();
			html.tr();
			html.th().text("Id").end();
			html.th().text("Tagger mention").end();

			html.th().text("Tagger start").end();

			html.th().text("Tagger end").end();

			html.th().text("Tagger entity").end();

			html.th().text("Tagger name").end();
			html.th().text("Tagger condidence").end();
			html.end();
			html.end();
			html.tbody();
			for (int i = 0; i < fp.size(); i++) {
				html.tr();
				html.td().text(String.valueOf(i + 1)).end();

				AnnotatedSpot t = fp.get(i);
				html.td().text(cleanMention(t.getSpot())).end();

				html.td().text("" + t.getStart()).end();

				html.td().text("" + t.getEnd()).end();

				html.td().text(t.getEntity()).end();

				html.td().text(t.getWikiname()).end();
				html.td().text(String.format("%.3f", t.getConfidenceScore()))
						.end();

				html.end();

			}
			html.endAll();
			id++;
			try {
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private String eval(String text) {
		return text.replaceAll("\n", "<br/>").replaceAll("\r", "<br/>");
	}

	public void setPartial(boolean isPartial) {
		partial = isPartial;

	}

	private String cleanMention(String m) {
		if (m == null || m.isEmpty())
			return m;
		m = m.replaceAll("%20", " ");
		m = m.replaceAll("%26", "&");

		return m;
	}

	public void end() {
		// html.div().classAttr("row").id("summary");
		// html.h2().text("Summary").end();
		// html.div().classAttr("sum col-md-offset-2 span10 main");
		// html.table().classAttr("main");
		// html.thead();
		// html.tr();
		// for (MetricValuesCollector<?> collector : collectors) {
		//
		// html.th().text(collector.getName()).end();
		//
		// }
		// html.end();
		// html.end();
		// html.tbody();
		// html.tr();
		// for (MetricValuesCollector<?> collector : collectors) {
		// html.td();
		// Object o = collector.getScore();
		// double f = 0;
		// if (o instanceof Integer) {
		// int i = ((Integer) o).intValue();
		// html.text(String.format("%d", i)).end();
		// continue;
		// }
		//
		// if (o instanceof Float) {
		// f = (Float) (o);
		// }
		// if (o instanceof Double) {
		// f = (Double) o;
		// }
		// html.text(String.format("%.3f", f)).end();
		//
		// }
		// html.end();
		// html.end();
		// html.end();
		// html.end();
		//
		// html.endAll();
		// try {
		// bw.close();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}

	public void register(MetricValuesCollector collector) {
		collectors.add(collector);

	}
}
