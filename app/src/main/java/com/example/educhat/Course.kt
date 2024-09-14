package com.example.educhat

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.annotation.DrawableRes
data class BarChartData(val day: String, val value: Int)
data class PieChartData(val category: String, val value: Int)
data class Course(
    val id: String,
    val title: String,
    val description: String,
    val duration: String,
    val documentsCount: Int,
    @DrawableRes val imageResId: Int, // Resource ID for the course image
    val barChartData: List<BarChartData>,
    val pieChartData: List<PieChartData>,
    val progressPercentage: Int, // Progress percentage (0 to 100)
    val activities: List<Activity>,
    val lessons: List<Lessonforcourse> // List of lessons associated with the course

)
data class Activity(
    val name: String,
    val courseName: String,
    val timeAgo: String)

data class Lessonforcourse(
    val id: Int,
    val title: String,
    val description: String,
    val sections: List<LessonSection> // Multiple sections per lesson
)
data class LessonSection(
    val sectionTitle: String,
    val sectionContent: String // Each section will contain a paragraph of text
)
data class UploadHistoryItem(
    val type: UploadType,  // Either FILE or LINK
    val uri: String,       // The URI of the file or link
    val timestamp: Long    // Time when the item was added
)

enum class UploadType {
    FILE,
    LINK
}


class CourseViewModel : ViewModel() {
    // Course data as a state list
    val courses = mutableStateListOf<Course>()
    init {
        courses.addAll(
            listOf(
                Course(
                    id = "1",
                    title = "Calculus 3",
                    description = "Advanced calculus course focusing on derivatives and integrals.",
                    duration = "2h 30m",
                    documentsCount = 5,
                    imageResId = R.drawable.calculus_bg, // Replace with your actual image resource
                    barChartData = listOf(
                        BarChartData("Mon", 4),
                        BarChartData("Tue", 6),
                        BarChartData("Wed", 5),
                        BarChartData("Thu", 7),
                        BarChartData("Fri", 9),
                        BarChartData("Sat", 6),
                        BarChartData("Sun", 3)
                    ),
                    pieChartData = listOf(
                        PieChartData("Documents", 30),
                        PieChartData("Videos", 40),
                        PieChartData("Images", 20),
                        PieChartData("Misc", 10)
                    ),
                    progressPercentage = 80,
                    activities = listOf(
                        Activity("Final Revision", "Calculus 3", "45mins ago"),
                        Activity("Final Revision", "Calculus 3", "45mins ago"),
                        Activity("Final Revision", "Calculus 3", "45mins ago")

                    ),
                    lessons = listOf(
                        Lessonforcourse(
                            id = 1,
                            title = "Understanding Derivatives",
                            description = "An introduction to derivatives in calculus.",
                            sections = listOf(
                                LessonSection(
                                    sectionTitle = "The Power Rule",
                                    sectionContent = """
                            The power rule is one of the most commonly used rules in differentiation. 
                            It applies when differentiating expressions of the form x^n. The rule is simple:
                            bring down the exponent and subtract one from it. This makes it especially useful 
                            for polynomials, where each term is differentiated separately using this rule.
                        """.trimIndent()
                                ),
                                LessonSection(
                                    sectionTitle = "The Chain Rule",
                                    sectionContent = """
                            The chain rule is used to differentiate compositions of functions. 
                            It is an essential tool in calculus, especially when dealing with more complex functions. 
                            The rule states that the derivative of a composition is the derivative of the outer function 
                            multiplied by the derivative of the inner function.
                        """.trimIndent()
                                ),
                                LessonSection(
                                    sectionTitle = "The Product and Quotient Rules",
                                    sectionContent = """
                            When functions are multiplied or divided, special rules must be applied. 
                            The product rule is used when two functions are multiplied together, 
                            while the quotient rule is used when one function is divided by another. 
                            Both of these rules are critical for handling complex problems in calculus.
                        """.trimIndent()
                                )
                            )
                        ),
                        Lessonforcourse(
                            id = 2,
                            title = "Integration Techniques",
                            description = "Learn advanced techniques for solving integrals.",
                            sections = listOf(
                                LessonSection(
                                    sectionTitle = "Integration by Parts",
                                    sectionContent = """
                            Integration by parts is a technique that comes from the product rule for differentiation. 
                            It is particularly useful when integrating products of functions. The formula for integration 
                            by parts is derived from reversing the product rule and can simplify otherwise difficult integrals.
                        """.trimIndent()
                                ),
                                LessonSection(
                                    sectionTitle = "Substitution Methods",
                                    sectionContent = """
                            Substitution is a widely used method to simplify integrals. By making a substitution for a complex 
                            part of the integral, we can transform the integral into a simpler form that is easier to evaluate. 
                            This method is especially useful when dealing with composite functions.
                        """.trimIndent()
                                ),
                                LessonSection(
                                    sectionTitle = "Partial Fraction Decomposition",
                                    sectionContent = """
                            Partial fraction decomposition is a technique used when integrating rational functions. 
                            It involves expressing a complex rational function as a sum of simpler fractions, which can 
                            then be integrated separately. This method is particularly helpful for rational functions 
                            with polynomial denominators.
                        """.trimIndent()
                                )
                            )
                        )
                    )
                ),



                Course(
                    id = "2",
                    title = "Linear Algebra 101",
                    description = "Basic course on linear algebra concepts such as matrices and vector spaces.",
                    duration = "1h 45m",
                    documentsCount = 7,
                    imageResId = R.drawable.cs_bg, // Replace with your actual image resource
                    barChartData = listOf(
                        BarChartData("Mon", 5),
                        BarChartData("Tue", 4),
                        BarChartData("Wed", 6),
                        BarChartData("Thu", 8),
                        BarChartData("Fri", 7),
                        BarChartData("Sat", 5),
                        BarChartData("Sun", 2)
                    ),
                    pieChartData = listOf(
                        PieChartData("Documents", 50),
                        PieChartData("Videos", 30),
                        PieChartData("Images", 10),
                        PieChartData("Misc", 10)
                    ),
                    progressPercentage = 50,
                    activities = listOf(
                        Activity("Final Revision", "CS305", "1hr ago"),
                        Activity("Final Revision", "CS305", "1hr ago"),
                        Activity("Final Revision", "Linear Algebra 101", "1hr ago")

                    ),
                    lessons = listOf(
                        Lessonforcourse(
                            id = 1,
                            title = "Introduction to Matrices",
                            description = "Learn the basics of matrices and matrix operations.",
                            sections = listOf(
                                LessonSection(
                                    sectionTitle = "Matrix Multiplication",
                                    sectionContent = """
                            Matrix multiplication is a fundamental operation in linear algebra. 
                            It is used to combine two matrices to produce a third matrix. This operation is not commutative, 
                            meaning the order of multiplication matters. Matrix multiplication is used extensively 
                            in areas such as computer graphics, physics, and machine learning.
                        """.trimIndent()
                                ),
                                LessonSection(
                                    sectionTitle = "Inverses and Determinants",
                                    sectionContent = """
                            The inverse of a matrix is the matrix that, when multiplied by the original matrix, 
                            produces the identity matrix. Determinants are used to find inverses and also provide 
                            information about the matrix's properties, such as whether it is invertible.
                        """.trimIndent()
                                ),
                                LessonSection(
                                    sectionTitle = "Matrix Addition and Scalar Multiplication",
                                    sectionContent = """
                            Matrix addition is a simple operation that involves adding the corresponding elements 
                            of two matrices. Scalar multiplication involves multiplying every element of a matrix 
                            by a scalar (a single number). These operations are the building blocks of more complex 
                            matrix manipulations and are essential for working with matrices in various applications.
                        """.trimIndent()
                                )
                            )
                        ),
                        Lessonforcourse(
                            id = 2,
                            title = "Vector Spaces and Linear Transformations",
                            description = "Explore the concept of vector spaces and linear transformations.",
                            sections = listOf(
                                LessonSection(
                                    sectionTitle = "Basis and Dimension",
                                    sectionContent = """
                            A basis of a vector space is a set of linearly independent vectors that span the entire space. 
                            The number of vectors in the basis is called the dimension of the space. 
                            Understanding basis and dimension is crucial for working with vector spaces in linear algebra.
                        """.trimIndent()
                                ),
                                LessonSection(
                                    sectionTitle = "Linear Independence",
                                    sectionContent = """
                            Linear independence refers to a set of vectors where no vector in the set can be expressed as 
                            a linear combination of the others. This concept is important in determining the basis of a 
                            vector space and plays a central role in many areas of mathematics and engineering.
                        """.trimIndent()
                                ),
                                LessonSection(
                                    sectionTitle = "Eigenvalues and Eigenvectors",
                                    sectionContent = """
                            Eigenvalues and eigenvectors are fundamental in understanding linear transformations. 
                            An eigenvector of a matrix is a vector that does not change direction under the transformation 
                            represented by the matrix, and the eigenvalue is the scalar that represents the magnitude of the change. 
                            These concepts are widely used in various fields, including physics and data science.
                        """.trimIndent()
                                )
                            )
                        )
                    )
                )
            )
        )
    }
    // Function to get a course by its ID
    fun getCourseById(courseId: String): Course? {
        return courses.find { it.id == courseId }
    }

    // Function to get a lesson by its ID within a course
    fun getLessonById(courseId: String, lessonId: Int): Lessonforcourse? {
        val course = getCourseById(courseId)
        return course?.lessons?.find { it.id == lessonId }
    }
}