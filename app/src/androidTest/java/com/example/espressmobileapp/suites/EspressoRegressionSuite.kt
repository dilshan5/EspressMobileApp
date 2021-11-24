package com.example.espressmobileapp.suites

import com.example.espressmobileapp.test.InstrumentedE2ETest
import com.example.espressmobileapp.test.InstrumentedIntegrationTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(InstrumentedIntegrationTest::class, InstrumentedE2ETest::class)
class EspressoRegressionSuite {
}