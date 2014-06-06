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

	boolean partial = false;
	MetricUtil utils = new MetricUtil();
	Html html = null;
	BufferedWriter bw = null;

	public HTMLResultsAppender(String htmlFile) {
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
				.text("$(function() {\n" + "$('.tp').tablesorter({ \n"
						+ "theme : 'blue'\n" + "});\n"
						+ "$('.fn').tablesorter({ \n" + "theme : 'blue'\n"
						+ "});\n" + "$('.fp').tablesorter({ \n"
						+ "theme : 'blue'\n" + "});\n" + "" + "});").end();

		html.end();
		html.body();
		html.div().classAttr("container");
		html.h1().text("Dexter Eval").end();

	}

	private static final Logger logger = LoggerFactory
			.getLogger(HTMLResultsAppender.class);

	public HTMLResultsAppender appendPartial() {
		partial = true;
		return this;
	}

	public void append(MetricValuesCollector<?> metric) {
		double score = metric.getScore();
		html.div().strong().text(metric.getName()).text(":").end();
		html.pre().text(String.valueOf(score)).end().end();
	}

	public boolean isAppendPartial() {
		return partial;
	}

	public void appendPartial(List<AnnotatedSpot> predictions,
			List<AnnotatedSpot> goldenTruth,
			AnnotatedSpotComparator comparator,
			List<MetricValuesCollector<?>> collectors) {
		if (goldenTruth.isEmpty()) {
			logger.warn("empty golden truth!");
		} else {
			html.div().classAttr("row").id(goldenTruth.get(0).getDocId()).h3()
					.text(goldenTruth.get(0).getDocId()).end();
			html.table();
			for (MetricValuesCollector<?> collector : collectors) {
				html.hr();
				html.th().text(collector.getName()).end();
				html.end();
			}

			for (MetricValuesCollector<?> collector : collectors) {
				html.tr();
				html.td().text("" + collector.getPartial()).end();
				html.end();
			}
			html.end();
		}

		List<AnnotatedSpot> tpPredictions = new ArrayList<AnnotatedSpot>();
		List<AnnotatedSpot> tpGoldenTruth = new ArrayList<AnnotatedSpot>();
		List<AnnotatedSpot> fp = new ArrayList<AnnotatedSpot>();
		List<AnnotatedSpot> fn = new ArrayList<AnnotatedSpot>();

		utils.intersect(predictions, goldenTruth, tpPredictions, tpGoldenTruth,
				fp, fn, comparator);
		html.div().classAttr("tp col-md-offset-2 span10 eval");
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
			html.td().text(s.getSpot()).end();
			html.td().text(t.getSpot()).end();

			html.td().text("" + s.getStart()).end();
			html.td().text("" + t.getStart()).end();

			html.td().text("" + s.getEnd()).end();
			html.td().text("" + t.getEnd()).end();

			html.td().text(s.getEntity()).end();
			html.td().text(t.getEntity()).end();

			html.td().text(s.getWikiname()).end();

			html.td().text(t.getWikiname()).end();
			html.td().text("" + t.getConfidenceScore()).end();

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
		html.th().text("Tagger mention").end();

		html.th().text("Tagger start").end();

		html.th().text("Tagger end").end();

		html.th().text("Tagger entity").end();

		html.th().text("Tagger name").end();
		html.th().text("Tagger condidence").end();
		html.end();
		html.end();
		html.tbody();
		for (int i = 0; i < fn.size(); i++) {
			html.tr();
			html.td().text(String.valueOf(i + 1)).end();

			AnnotatedSpot t = fn.get(i);
			html.td().text(t.getSpot()).end();

			html.td().text("" + t.getStart()).end();

			html.td().text("" + t.getEnd()).end();

			html.td().text(t.getEntity()).end();

			html.td().text(t.getWikiname()).end();
			html.td().text("" + t.getConfidenceScore()).end();

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
			html.td().text(t.getSpot()).end();

			html.td().text("" + t.getStart()).end();

			html.td().text("" + t.getEnd()).end();

			html.td().text(t.getEntity()).end();

			html.td().text(t.getWikiname()).end();
			html.td().text("" + t.getConfidenceScore()).end();

			html.end();

		}
		html.end();
		html.end();
		html.end();

		html.end();

	}

	public void setPartial(boolean isPartial) {
		partial = isPartial;

	}

	public void end() {
		html.endAll();
		try {
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
