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

import static org.junit.Assert.assertEquals;
import it.cnr.isti.hpc.dexter.eval.AnnotatedSpot;
import it.cnr.isti.hpc.dexter.eval.cmp.WeakMentionComparator;
import it.cnr.isti.hpc.dexter.eval.metrics.FMeasureMetric;
import it.cnr.isti.hpc.dexter.eval.metrics.PrecisionMetric;
import it.cnr.isti.hpc.dexter.eval.metrics.RecallMetric;
import it.cnr.isti.hpc.dexter.eval.metrics.TruePositiveMetric;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Diego Ceccarelli <diego.ceccarelli@isti.cnr.it>
 * 
 *         Created on Feb 17, 2014
 */
public class CollectorsTests {
	private static List<AnnotatedSpot> goldenTruth;
	private static List<AnnotatedSpot> predictions;
	private static List<AnnotatedSpot> predictions2;
	private static List<AnnotatedSpot> predictions3;

	WeakMentionComparator comparator = new WeakMentionComparator();

	@BeforeClass
	public static void setUp() {
		goldenTruth = new ArrayList<AnnotatedSpot>();
		predictions = new ArrayList<AnnotatedSpot>();
		predictions2 = new ArrayList<AnnotatedSpot>();
		predictions3 = new ArrayList<AnnotatedSpot>();
		goldenTruth.add(new AnnotatedSpot("da vinci"));
		goldenTruth.add(new AnnotatedSpot("picasso"));
		goldenTruth.add(new AnnotatedSpot("raffaello"));
		goldenTruth.add(new AnnotatedSpot("bosch"));

		predictions.add(new AnnotatedSpot("picasso"));
		predictions.add(new AnnotatedSpot("michelangelo"));
		predictions.add(new AnnotatedSpot("matisse"));

		predictions2.add(new AnnotatedSpot("bosch"));
		predictions2.add(new AnnotatedSpot("raffaello"));

		predictions3.add(new AnnotatedSpot("munch"));
		predictions3.add(new AnnotatedSpot("chagall"));
		predictions3.add(new AnnotatedSpot("michelangelo"));
		predictions3.add(new AnnotatedSpot("botticelli"));
		predictions3.add(new AnnotatedSpot("modigliani"));

	}

	@Test
	public void macroTruePositiveCollectorTest() {
		IntValuesCollector collector = new IntValuesCollector(
				new TruePositiveMetric());
		collector.collect(predictions, goldenTruth, comparator);
		collector.collect(predictions2, goldenTruth, comparator);
		collector.collect(predictions3, goldenTruth, comparator);
		assertEquals(1 + 2 + 0, collector.getCollectedTotal(), 0.001);
		assertEquals((1 + 2 + 0) / 3, collector.getScore(), 0.001);
	}

	@Test
	public void macroPrecisionCollectorTest() {
		DoubleValuesCollector precision = new DoubleValuesCollector(
				new PrecisionMetric());
		precision.collect(predictions, goldenTruth, comparator);
		precision.collect(predictions2, goldenTruth, comparator);
		precision.collect(predictions3, goldenTruth, comparator);
		assertEquals(1.0 / 3.0 + 1.0, precision.getCollectedTotal(), 0.0001);
		assertEquals((1.0 / 3.0 + 1.0) / 3.0, precision.getScore(), 0.0001);
	}

	@Test
	public void macroRecallCollectorTest() {
		DoubleValuesCollector recall = new DoubleValuesCollector(
				new RecallMetric());
		recall.collect(predictions, goldenTruth, comparator);
		recall.collect(predictions2, goldenTruth, comparator);
		recall.collect(predictions3, goldenTruth, comparator);
		assertEquals(1.0 / 4.0 + 0.5, recall.getCollectedTotal(), 0.0001);
		assertEquals((1.0 / 4.0 + 0.5) / 3.0, recall.getScore(), 0.0001);
	}

	@Test
	public void macroFMeasureCollectorTest() {
		DoubleValuesCollector collector = new DoubleValuesCollector(
				new FMeasureMetric());
		collector.collect(predictions, goldenTruth, comparator);
		collector.collect(predictions2, goldenTruth, comparator);
		collector.collect(predictions3, goldenTruth, comparator);
		double f1prediction = 2 * (1.0 / 4.0 * 1.0 / 3.0)
				/ (1.0 / 4.0 + 1.0 / 3.0);
		double f1prediction2 = (2 * 1.0 / 2.0) / (1 + 1.0 / 2.0);
		double f2prediction3 = 0.0;

		assertEquals(f1prediction + f1prediction2 + f2prediction3,
				collector.getCollectedTotal(), 0.0001);
		assertEquals((f1prediction + f1prediction2 + f2prediction3) / 3,
				collector.getScore(), 0.0001);
	}

	@Test
	public void microPrecisionCollectorTest() {
		MicroPrecisionValuesCollector collector = new MicroPrecisionValuesCollector();
		collector.collect(predictions, goldenTruth, comparator);
		collector.collect(predictions2, goldenTruth, comparator);
		collector.collect(predictions3, goldenTruth, comparator);
		double tp = 1.0;
		double tp2 = 2.0;
		double tp3 = 0.0;
		double fp = 2.0;
		double fp2 = 0.0;
		double fp3 = 5.0;
		double totalTp = tp + tp2 + tp3;
		double totalFp = fp + fp2 + fp3;

		assertEquals(totalTp / (totalTp + totalFp),
				collector.getCollectedTotal(), 0.0001);

	}

	@Test
	public void microRecallCollectorTest() {
		MicroRecallValuesCollector collector = new MicroRecallValuesCollector();
		collector.collect(predictions, goldenTruth, comparator);
		collector.collect(predictions2, goldenTruth, comparator);
		collector.collect(predictions3, goldenTruth, comparator);
		double tp = 1.0;
		double tp2 = 2.0;
		double tp3 = 0.0;
		double fn = 3.0;
		double fn2 = 2.0;
		double fn3 = 4.0;
		double totalTp = tp + tp2 + tp3;
		double totalFn = fn + fn2 + fn3;

		assertEquals(totalTp / (totalTp + totalFn),
				collector.getCollectedTotal(), 0.0001);

	}

	@Test
	public void microFmeasureCollectorTest() {
		MicroFMeasureValuesCollector collector = new MicroFMeasureValuesCollector();
		collector.collect(predictions, goldenTruth, comparator);
		collector.collect(predictions2, goldenTruth, comparator);
		collector.collect(predictions3, goldenTruth, comparator);
		double tp = 1.0;
		double tp2 = 2.0;
		double tp3 = 0.0;
		double fn = 3.0;
		double fn2 = 2.0;
		double fn3 = 4.0;
		double fp = 2.0;
		double fp2 = 0.0;
		double fp3 = 5.0;
		double totalTp = tp + tp2 + tp3;
		double totalFp = fp + fp2 + fp3;
		double totalFn = fn + fn2 + fn3;
		double precision = totalTp / (totalTp + totalFp);
		double recall = totalTp / (totalTp + totalFn);

		assertEquals(2 * (precision * recall) / (precision + recall),
				collector.getCollectedTotal(), 0.0001);

	}

}
