package com.lcx.sunnyweather

import android.app.Activity
import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * 封装ViewBinding
 */

/**
 * 在activity中使用
 */
inline fun <reified VB: ViewBinding> Activity.inflate() = lazy {
    inflateBinding<VB>(layoutInflater).apply { setContentView(root) }
}
/**
 * 在Dialog中使用
 */
inline fun <reified VB: ViewBinding> Dialog.inflate() = lazy {
    inflateBinding<VB>(layoutInflater).apply { setContentView(root) }
}

/**
 * 通过反射调用inflate方法来实现在Activity和Dialog中ViewBinding的封装
 */
inline fun <reified VB: ViewBinding> inflateBinding(layoutInflater: LayoutInflater) =
    VB::class.java.getMethod("inflate",LayoutInflater::class.java).invoke(null, layoutInflater) as VB

/**
 * 在fragment中使用
 */
inline fun <reified VB: ViewBinding> Fragment.bindView() = FragmentBindingDelegate(VB::class.java)

/**
 * 通过实现只读属性的委托接口来实现在Fragment中ViewBinding的封装和释放
 */
class FragmentBindingDelegate<VB:ViewBinding>(private val clazz: Class<VB>):
        ReadOnlyProperty<Fragment, VB>{
    private var isInitialized = false
    private var _binding: VB? = null
    private val binding get() = _binding!!
    override fun getValue(thisRef: Fragment, property: KProperty<*>): VB {
        if (!isInitialized) {
            thisRef.viewLifecycleOwner.lifecycle.addObserver(object : LifecycleObserver {
                @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
                fun onDestroyView() {
                    _binding = null
                }
            })
            _binding = clazz.getMethod("bind",View::class.java)
                .invoke(null, thisRef.requireView()) as VB
            isInitialized = true
        }
        return binding
    }
}

class BindingViewHolder<VB: ViewBinding>(val binding: VB): RecyclerView.ViewHolder(binding.root)

inline fun<reified T: ViewBinding> newBindingViewHolder(parent: ViewGroup) : BindingViewHolder<T>{
    val method = T::class.java.getMethod("inflate",LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)
    val binding = method.invoke(null, LayoutInflater.from(parent.context), parent, false) as T
    return BindingViewHolder(binding)
}
