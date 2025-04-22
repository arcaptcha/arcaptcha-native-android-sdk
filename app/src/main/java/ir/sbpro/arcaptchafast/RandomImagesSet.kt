package ir.sbpro.arcaptchafast

class RandomImagesSet {
    companion object {
        fun generate(count: Int) : List<String> {
            val list = ArrayList<String>()
            val selectedSet = HashSet<Int>()

            for(i in 0 until 9){
                var randIndex = (1..706).random()
                while (selectedSet.contains(randIndex)) randIndex = (1..706).random()

                selectedSet.add(randIndex)
                list.add("https://yavuzceliker.github.io/sample-images/image-${randIndex}.jpg")
            }

            return list
        }
    }
}