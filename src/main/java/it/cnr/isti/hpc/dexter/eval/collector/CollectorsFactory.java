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
package it.cnr.isti.hpc.dexter.eval.collector;

import it.cnr.isti.hpc.dexter.eval.filter.Filter;
import it.cnr.isti.hpc.dexter.eval.filter.ThresholdFilter;
import it.cnr.isti.hpc.dexter.eval.filter.TopKFilter;
import it.cnr.isti.hpc.dexter.eval.metrics.FMeasureMetric;
import it.cnr.isti.hpc.dexter.eval.metrics.FalseNegativeMetric;
import it.cnr.isti.hpc.dexter.eval.metrics.FalsePositiveMetric;
import it.cnr.isti.hpc.dexter.eval.metrics.PrecisionMetric;
import it.cnr.isti.hpc.dexter.eval.metrics.RecallMetric;
import it.cnr.isti.hpc.dexter.eval.metrics.TruePositiveMetric;
import it.cnr.isti.hpc.dexter.eval.output.ConsoleResultsAppender;
import it.cnr.isti.hpc.dexter.eval.output.HTMLResultsAppender;
import it.cnr.isti.hpc.dexter.eval.output.OutputResultsAppender;
import it.cnr.isti.hpc.io.IOUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Diego Ceccarelli <diego.ceccarelli@isti.cnr.it>
 * 
 *         Created on Feb 19, 2014
 */
public class CollectorsFactory {

	private static final Logger logger = LoggerFactory
			.getLogger(CollectorsFactory.class);

	public CollectorsFactory() {

	}

	public MetricValuesCollector<?> newCollector(String code) {
		if (!code.contains("["))
			return getCollector(code);
		String[] pieces = code.split("\\[");
		MetricValuesCollector<?> collector = getCollector(pieces[0]);
		if (collector == null)
			return null;
		List<OutputResultsAppender> outputs = new ArrayList<OutputResultsAppender>();
		for (int i = 1; i < pieces.length; i++) {
			String f = pieces[i];
			f = f.substring(0, f.length() - 1);
			f = f.trim();
			Filter filter = getFilter(f);
			if (filter != null) {
				collector.addFilter(filter);
				continue;
			}
			OutputResultsAppender a = getOutputResultsAppend(f);
			if (a != null) {
				outputs.add(a);
			} else {
				logger.warn("error processing param {}, ignoring", f);

			}

		}
		if (!outputs.isEmpty()) {
			collector.resetOutputCollectors();
			for (OutputResultsAppender ora : outputs) {
				collector.addOutputCollector(ora);
			}
		}

		return collector;
	}

	private OutputResultsAppender getOutputResultsAppend(String f) {
		OutputResultsAppender out = null;
		if (f.charAt(0) == '>') {
			if (f.charAt(1) == 'c') {
				out = new ConsoleResultsAppender();
			}

			if (f.charAt(1) == 'h') {
				out = new HTMLResultsAppender("eval.html");
			}
		}
		if (out != null) {
			if (f.charAt(f.length() - 1) == '+') {
				out.setPartial(true);
			}
		}

		return out;
	}

	public List<MetricValuesCollector<?>> newCollectors(File f)
			throws IOException {
		if (!f.exists()) {
			logger.warn("cannot find collectors file {}", f.getAbsolutePath());
			return Collections.emptyList();
		}
		BufferedReader br = IOUtils.getPlainOrCompressedReader(f
				.getAbsolutePath());
		List<String> codes = new ArrayList<String>();
		for (String line = br.readLine(); line != null; line = br.readLine()) {
			codes.add(line.trim());
		}
		return newCollectors(codes);

	}

	public List<MetricValuesCollector<?>> newCollectors(List<String> codes) {
		List<MetricValuesCollector<?>> collectors = new LinkedList<MetricValuesCollector<?>>();
		for (String code : codes) {
			MetricValuesCollector<?> collector = newCollector(code);
			if (collector == null) {
				logger.warn("cannot find collector for code {} ", code);
				continue;
			}
			collectors.add(collector);
		}
		return collectors;

	}

	private MetricValuesCollector<?> getCollector(String name) {
		if (name.equals("P")) {
			return new DoubleValuesCollector(new PrecisionMetric());
		}
		if (name.equals("R")) {
			return new DoubleValuesCollector(new RecallMetric());
		}
		if (name.equals("F1")) {
			return new DoubleValuesCollector(new FMeasureMetric());
		}
		if (name.equals("microP")) {
			return new MicroPrecisionValuesCollector();
		}
		if (name.equals("microR")) {
			return new MicroRecallValuesCollector();
		}
		if (name.equals("microF1")) {
			return new MicroFMeasureValuesCollector();
		}
		if (name.equals("tp")) {
			return new IntValuesCollector(new TruePositiveMetric());
		}
		if (name.equals("fp")) {
			return new IntValuesCollector(new FalsePositiveMetric());
		}
		if (name.equals("fn")) {
			return new IntValuesCollector(new FalseNegativeMetric());
		}
		logger.error("cannot find collector with name {} ", name);
		return null;

	}

	private Filter getFilter(String filter) {
		int k = 1;
		if (filter.charAt(0) == '@') {
			try {
				k = Integer.parseInt(filter.substring(1));
			} catch (NumberFormatException e) {
				logger.error("error parsing the topk filter");
				return null;
			}
			TopKFilter f = new TopKFilter(k);
			return f;
		}
		if (filter.charAt(0) == 't') {
			float threshold;
			try {
				threshold = Float.parseFloat(filter.substring(1));
			} catch (NumberFormatException e) {
				logger.error("error parsing the topk filter");
				return null;
			}
			ThresholdFilter f = new ThresholdFilter(threshold);
			return f;
		}
		return null;
	}

	public static void main(String[] args) {
		CollectorsFactory cf = new CollectorsFactory();
		System.out.println(cf.newCollector("tp[t0.5][@5]").getName());
	}

}
