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
package it.cnr.isti.hpc.dexter.eval.metrics;

import static org.junit.Assert.assertEquals;
import it.cnr.isti.hpc.dexter.eval.AnnotatedSpot;
import it.cnr.isti.hpc.dexter.eval.cmp.WeakMentionComparator;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Diego Ceccarelli <diego.ceccarelli@isti.cnr.it>
 * 
 *         Created on Feb 16, 2014
 */
public class MetricTests {

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
	public void truePositiveTests() {
		TruePositiveMetric metric = new TruePositiveMetric();
		assertEquals(new Integer(1),
				metric.eval(predictions, goldenTruth, comparator));
		assertEquals(new Integer(2),
				metric.eval(predictions2, goldenTruth, comparator));
		assertEquals(new Integer(0),
				metric.eval(predictions3, goldenTruth, comparator));

	}

	@Test
	public void falseNegativeTests() {
		FalseNegativeMetric metric = new FalseNegativeMetric();
		assertEquals(new Integer(3),
				metric.eval(predictions, goldenTruth, comparator));
		assertEquals(new Integer(2),
				metric.eval(predictions2, goldenTruth, comparator));
		assertEquals(new Integer(4),
				metric.eval(predictions3, goldenTruth, comparator));

	}

	@Test
	public void falsePositiveTests() {
		FalsePositiveMetric metric = new FalsePositiveMetric();
		assertEquals(new Integer(2),
				metric.eval(predictions, goldenTruth, comparator));
		assertEquals(new Integer(0),
				metric.eval(predictions2, goldenTruth, comparator));
		assertEquals(new Integer(5),
				metric.eval(predictions3, goldenTruth, comparator));

	}

	@Test
	public void precisionTest() {
		PrecisionMetric metric = new PrecisionMetric();
		assertEquals(1.0 / 3.0,
				metric.eval(predictions, goldenTruth, comparator), 0.001);
		assertEquals(1.0, metric.eval(predictions2, goldenTruth, comparator),
				0.001);
		assertEquals(0.0, metric.eval(predictions3, goldenTruth, comparator),
				0.001);

	}

	@Test
	public void recallTest() {
		RecallMetric metric = new RecallMetric();
		assertEquals(1.0 / 4.0,
				metric.eval(predictions, goldenTruth, comparator), 0.001);
		assertEquals(2.0 / (2.0 + 2.0),
				metric.eval(predictions2, goldenTruth, comparator), 0.001);
		assertEquals(0, metric.eval(predictions3, goldenTruth, comparator),
				0.001);

	}

	@Test
	public void fmeasureTest() {
		FMeasureMetric metric = new FMeasureMetric();
		assertEquals(2 * (1.0 / 4.0 * 1.0 / 3.0) / (1.0 / 4.0 + 1.0 / 3.0),
				metric.eval(predictions, goldenTruth, comparator), 0.001);
		assertEquals((2 * 1.0 / 2.0) / (1 + 1.0 / 2.0),
				metric.eval(predictions2, goldenTruth, comparator), 0.001);
		assertEquals(0, metric.eval(predictions3, goldenTruth, comparator),
				0.001);

	}
}
