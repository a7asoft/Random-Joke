package com.awto.randomjoke.util

import android.view.View
import androidx.fragment.app.Fragment
import com.awto.randomjoke.R
import com.awto.randomjoke.data.remote.model.JokeResponseModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup


fun Fragment.buildChips(data: JokeResponseModel, chipGroup: ChipGroup) {
    chipGroup.removeAllViews()

    //main joke category
    val chip = Chip(requireContext())
    chip.text = data.category
    chipGroup.addView(chip)

    if (data.flags.nsfw) {
        val chipNsfw = Chip(requireContext())
        chipNsfw.text = getString(R.string.nsfw)
        chipGroup.addView(chipNsfw)
    }

    if (data.flags.explicit) {
        val chipExplicit = Chip(requireContext())
        chipExplicit.text = getString(R.string.explicit)
        chipGroup.addView(chipExplicit)
    }

    if (data.flags.racist) {
        val chipRacist = Chip(requireContext())
        chipRacist.text = getString(R.string.racist)
        chipGroup.addView(chipRacist)
    }

    if (data.flags.political) {
        val chipPolitical = Chip(requireContext())
        chipPolitical.text = getString(R.string.political)
        chipGroup.addView(chipPolitical)
    }

    if (data.flags.sexist) {
        val chipSexist = Chip(requireContext())
        chipSexist.text = getString(R.string.sexist)
        chipGroup.addView(chipSexist)
    }

    if (data.flags.religious) {
        val chipReligious = Chip(requireContext())
        chipReligious.text = getString(R.string.religious)
        chipGroup.addView(chipReligious)
    }
}

fun View.showViewWithAnimation() {
    this.apply {
        alpha = 0f
        visibility = View.VISIBLE
        animate()
            .alpha(1f)
            .setDuration(200)
            .setListener(null)
    }
}

fun View.hideViewWithAnimation() {
    this.apply {
        alpha = 1f
        visibility = View.GONE
        animate()
            .alpha(0f)
            .setDuration(200)
            .setListener(null)
    }
}