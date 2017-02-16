Android Gauge Library
==========

A collection of custom gauges for Android devices

[![Build Unstable][shield-unstable]](#)
[![MIT licensed][shield-license]](#)



Table Of Contents
-----------------

- [Requirements](#requirements)
- [Usage](#usage)
- [Screenshots](#screenshots)
- [License](#license)


Requirements
-------
  * [Android Studio][android-studio]
  * Android Lollipop API 21+

Usage
-----

```xml
<!-- res/layout/activity.xml -->
<com.bptracker.ui.gauges.GradientCircleGauge
    android:id="@+id/gcg_gauge"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:layout_weight="1"
    gauge:backgroundVisible="false"
    gauge:titleText="Battery"
    gauge:titleTextRadius="73dp"
    gauge:titleTextSize="15sp"
    gauge:animation="true"
    gauge:gaugeLevel="50"
    gauge:additionalLabel="CHARGE CYCLE"
    gauge:additionalValue="232323"
    gauge:progressLabel="RESERVE"
    gauge:foregroundStartColor="#125D9D"
    gauge:foregroundEndColor="#125D9D" />
```

```java
  // Activity class
  mGauge = (GradientCircleGauge) findViewById(R.id.gcg_gauge);
  mGauge.setAdditionalValue("19");
  mGauge.setAdditionalLabel("CHARGE CYCLE");
  mGauge.setGaugeLevel(54);
```

See also the included example app.

Screenshots
-----
<img width="200px" src="https://rawgit.com/BP-Tracker/android-gauge-library/master/docs/images/gauges.png" alt="Screenshot" />

License
-------

Android Gauge Library is licensed under the [MIT][info-license] license.  
Copyright &copy; 2017 Derek Benda

[android-studio]: https://developer.android.com/studio/index.html
[shield-unstable]: https://img.shields.io/badge/build-unstable-red.svg
[shield-license]: https://img.shields.io/badge/license-MIT-blue.svg

[info-license]: LICENSE
