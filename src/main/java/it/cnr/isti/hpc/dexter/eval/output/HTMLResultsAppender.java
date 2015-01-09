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
import it.cnr.isti.hpc.io.IOUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
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
public class HTMLResultsAppender implements OutputResultsAppender {

	boolean partial = true;
	MetricUtil utils = new MetricUtil();
	Html html = null;
	BufferedWriter bw = null;

	List<MetricValuesCollector<?>> collectors = new LinkedList<MetricValuesCollector<?>>();

	public HTMLResultsAppender(String htmlFile,
			AnnotatedSpotComparator comparator) {
		bw = IOUtils.getPlainOrCompressedUTF8Writer(htmlFile);
		html = new Html(bw);
		html.html();
		html.head();
		html.title("Dexter Eval");

		html.link().href("http://getbootstrap.com/dist/css/bootstrap.min.css")
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
				.text("$(function () {$('.detail').hide();}); \n\n"
						+ "$(function() {\n"
						+ "$('.tp').tablesorter({ \n"
						+ "theme : 'blue'\n"
						+ "});\n"
						+ "$('.fn').tablesorter({ \n"
						+ "theme : 'blue'\n"
						+ "});\n"
						+ "$('.fp').tablesorter({ \n"
						+ "theme : 'blue'\n"
						+ "});\n"
						+ "$('.main').tablesorter({ \n"
						+ "theme : 'blue'\n"
						+ "});"
						+ "});\n"
						+ "\n"
						+ "\n"
						+ "function moar(text){  $('#'+text+'-detail').show(1000);}\n\nfunction less(text){ $('#'+text+'-detail').hide(1000);}")
				.end();

		html.end();
		html.body();
		html.div().classAttr("container");
		html.h1().text("Dexter Eval [" + htmlFile + "]").end();
		html.strong().text("Comparator: ").end();
		html.p()
				.text(comparator.getName() + ": " + comparator.getDescription())
				.end();

	}

	private static final Logger logger = LoggerFactory
			.getLogger(HTMLResultsAppender.class);

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

		if (goldenTruth.isEmpty()) {
			logger.warn("empty golden truth!");
		} else {
			html.div().classAttr("row").id(goldenTruth.get(0).getDocId()).h3()
					.text(goldenTruth.get(0).getDocId());
			html.a().onclick("moar('" + goldenTruth.get(0).getDocId() + "')")
					.text("[more]").end().a()
					.onclick("less('" + goldenTruth.get(0).getDocId() + "')")
					.text("[less]").end().end();
			html.div().classAttr("sum col-md-offset-2 span10 main");
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
			html.end();
		}

		List<AnnotatedSpot> tpPredictions = new ArrayList<AnnotatedSpot>();
		List<AnnotatedSpot> tpGoldenTruth = new ArrayList<AnnotatedSpot>();
		List<AnnotatedSpot> fp = new ArrayList<AnnotatedSpot>();
		List<AnnotatedSpot> fn = new ArrayList<AnnotatedSpot>();

		utils.intersect(predictions, goldenTruth, tpPredictions, tpGoldenTruth,
				fp, fn, comparator);

		html.div().id(goldenTruth.get(0).getDocId() + "-detail")
				.classAttr("detail");
		html.div().classAttr("tp col-md-offset-2 span10 eval ");
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
			html.td().text(String.format("%.3f", t.getConfidenceScore())).end();

			html.end();
		}
		html.end();
		html.end();
		html.end();
		html.div().classAttr("fn col-md-offset-2 span10 eval");

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

		html.div().classAttr("fp col-md-offset-2 span10 eval");
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
			html.td().text(String.format("%.3f", t.getConfidenceScore())).end();

			html.end();

		}
		html.end();
		html.end();
		html.end();
		html.end();
		html.end();

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
		html.div().classAttr("row").id("summary");
		html.h2().text("Summary").end();
		html.div().classAttr("sum col-md-offset-2 span10 main");
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
			Object o = collector.getScore();
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
		html.end();

		html.endAll();
		try {
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void register(MetricValuesCollector collector) {
		collectors.add(collector);

	}

	public void appendPartial(String text, List<AnnotatedSpot> prediction,
			List<AnnotatedSpot> goldenTruth, AnnotatedSpotComparator comparator) {
		appendPartial(prediction, goldenTruth, comparator);
	}
}
