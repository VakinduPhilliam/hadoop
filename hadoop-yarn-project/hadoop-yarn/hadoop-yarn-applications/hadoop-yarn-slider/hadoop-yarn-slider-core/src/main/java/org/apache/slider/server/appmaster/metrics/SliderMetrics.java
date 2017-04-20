/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.slider.server.appmaster.metrics;

import com.codahale.metrics.Counter;
import org.apache.hadoop.metrics2.MetricsCollector;
import org.apache.hadoop.metrics2.MetricsInfo;
import org.apache.hadoop.metrics2.MetricsSource;
import org.apache.hadoop.metrics2.MetricsSystem;
import org.apache.hadoop.metrics2.annotation.Metric;
import org.apache.hadoop.metrics2.annotation.Metrics;
import org.apache.hadoop.metrics2.lib.DefaultMetricsSystem;
import org.apache.hadoop.metrics2.lib.MetricsRegistry;
import org.apache.hadoop.metrics2.lib.MutableGaugeInt;

import java.util.HashMap;
import java.util.Map;

import static org.apache.hadoop.metrics2.lib.Interns.info;

@Metrics(context = "yarn-native-service")
public class SliderMetrics implements MetricsSource {

  @Metric("containers requested")
  public MutableGaugeInt containersRequested;
  @Metric("anti-affinity containers pending")
  public MutableGaugeInt pendingAAContainers;
  @Metric("containers running")
  public MutableGaugeInt containersRunning;
  @Metric("containers desired")
  public MutableGaugeInt containersDesired;
  @Metric("containers completed")
  public MutableGaugeInt containersCompleted;
  @Metric("containers failed")
  public MutableGaugeInt containersFailed;
  @Metric("containers failed since last threshold")
  public MutableGaugeInt failedSinceLastThreshold;
  @Metric("containers preempted")
  public MutableGaugeInt containersPreempted;
  @Metric("containers exceeded limits")
  public MutableGaugeInt containersLimitsExceeded;
  @Metric("containers surplus")
  public MutableGaugeInt surplusContainers;
  @Metric("containers failed due to disk failure")
  public MutableGaugeInt containersDiskFailure;

  protected final MetricsRegistry registry;

  public SliderMetrics(MetricsInfo metricsInfo) {
    registry = new MetricsRegistry(metricsInfo);
  }

  @Override
  public void getMetrics(MetricsCollector collector, boolean all) {
    registry.snapshot(collector.addRecord(registry.info()), all);
  }

  public static SliderMetrics register(String name, String description) {
    SliderMetrics metrics = new SliderMetrics(info(name, description));
    DefaultMetricsSystem.instance().register(name, description, metrics);
    return metrics;
  }

  public void tag(String name, String description, String value) {
    registry.tag(name, description, value);
  }
}

