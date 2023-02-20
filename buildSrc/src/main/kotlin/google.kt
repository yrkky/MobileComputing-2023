object google {

    object accompanist {
        private val version = "0.21.0-beta"

        val insets = "com.google.accompanist:accompanist-insets:$version"
    }

    object dagger {
        object hilt {
            private val version = "2.44.2"
            private val compiler_version = "2.44"

            val android = "com.google.dagger:hilt-android:$version"
            val compiler = "com.google.dagger:hilt-android-compiler:$compiler_version"
        }
    }

}
