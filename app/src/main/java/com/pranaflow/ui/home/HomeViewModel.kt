package com.pranaflow.ui.home

import androidx.lifecycle.ViewModel
import com.pranaflow.model.TechniqueCategory
import com.pranaflow.model.Techniques
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    val quickCalmTechniqueId: String = Techniques.boxBreathing.id
    val categories: List<TechniqueCategory> = TechniqueCategory.entries
}
