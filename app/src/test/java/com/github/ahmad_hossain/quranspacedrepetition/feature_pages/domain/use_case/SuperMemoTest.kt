package com.github.ahmad_hossain.quranspacedrepetition.feature_pages.domain.use_case

import com.github.ahmad_hossain.quranspacedrepetition.feature_pages.domain.model.Page
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class SuperMemoTest {

    @ParameterizedTest(name = "grade: {0}, inputPage: {1}, expectedPage: {2}")
    @MethodSource("pagesProvider")
    fun `SuperMemo calculates interval, repetitions, and eFactor correctly`(
        grade: Int,
        inputPage: Page,
        expectedPage: Page
    ) {
        val sm = SuperMemo()

        val res = sm(
            page = inputPage,
            grade = grade
        )

        assertThat(res.interval).isEqualTo(expectedPage.interval)
        assertThat(res.repetitions).isEqualTo(expectedPage.repetitions)
        assertThat(res.eFactor).isEqualTo(expectedPage.eFactor)
    }

    companion object {
        @JvmStatic
        fun pagesProvider(): Stream<Arguments> {
            val pageNum = 0
            return Stream.of(
                Arguments.of(
                    5,
                    Page(pageNum, interval = 6, repetitions = 2, eFactor = 1.3),
                    Page(pageNum, interval = 8, repetitions = 3, eFactor = 1.4000000000000001)
                ),
                Arguments.of(
                    5,
                    Page(pageNum, interval = 0, repetitions = 0, eFactor = 2.5),
                    Page(pageNum, interval = 1, repetitions = 1, eFactor = 2.6)
                ),
                Arguments.of(
                    4,
                    Page(pageNum, interval = 1, repetitions = 1, eFactor = 2.6),
                    Page(pageNum, interval = 6, repetitions = 2, eFactor = 2.6)
                ),
                Arguments.of(
                    3,
                    Page(pageNum, interval = 6, repetitions = 2, eFactor = 2.6),
                    Page(pageNum, interval = 16, repetitions = 3, eFactor = 2.46)
                ),
                Arguments.of(
                    2,
                    Page(pageNum, interval = 16, repetitions = 3, eFactor = 2.46),
                    Page(pageNum, interval = 1, repetitions = 0, eFactor = 2.1399999999999997)
                ),
                Arguments.of(
                    1,
                    Page(pageNum, interval = 1, repetitions = 0, eFactor = 2.1399999999999997),
                    Page(pageNum, interval = 1, repetitions = 0, eFactor = 1.5999999999999996)
                ),
                Arguments.of(
                    0,
                    Page(pageNum, interval = 1, repetitions = 0, eFactor = 1.5999999999999996),
                    Page(pageNum, interval = 1, repetitions = 0, eFactor = 1.3)
                ),
            )
        }
    }
}