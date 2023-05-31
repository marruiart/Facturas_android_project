package com.example.facturas.data.model

import android.graphics.Color
import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import com.example.facturas.App
import com.example.facturas.R
import com.example.facturas.data.database.entity.InvoiceEntity
import com.example.facturas.utils.MyConstants
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class InvoiceVO : Parcelable, Invoice {
    private var id = 0
    private var descEstado: String? = null
    private val importeOrdenacion: Float
    private val fecha: LocalDate
    private var textColor: Int?

    constructor(descEstado: String?, importeOrdenacion: Float, fecha: LocalDate, textColor: Int? = null) {
        setDescEstado(descEstado)
        this.importeOrdenacion = importeOrdenacion
        this.fecha = fecha
        this.textColor = textColor
    }

    fun toInvoiceEntity(): InvoiceEntity {
        return InvoiceEntity(descEstado, importeOrdenacion, fecha, textColor)
    }

    // Constructor to read from Parcel
    protected constructor(`in`: Parcel) {
        id = `in`.readInt()
        descEstado = `in`.readString()
        importeOrdenacion = `in`.readFloat()
        fecha = LocalDate.parse(`in`.readString())
        textColor = `in`.readInt()
    }

    override fun getId(): Int {
        return id
    }

    fun setId(id: Int) {
        this.id = id
    }

    override fun getDescEstado(): String {
        return descEstado!!
    }

    fun setDescEstado(descEstado: String?) {
        val context = App.getContext().resources
        textColor = Color.BLACK
        when (descEstado) {
            MyConstants.INVOICE_DESC_ESTADO_PAGADA -> this.descEstado = context.getString(
                R.string.fragment_filter_cb_paid
            )

            MyConstants.INVOICE_DESC_ESTADO_ANULADA -> this.descEstado =
                context.getString(R.string.fragment_filter_cb_canceled)

            MyConstants.INVOICE_DESC_ESTADO_CUOTA_FIJA -> this.descEstado =
                context.getString(R.string.fragment_filter_cb_fixed_fee)

            MyConstants.INVOICE_DESC_ESTADO_PENDIENTE -> {
                textColor = Color.RED
                this.descEstado = context.getString(R.string.fragment_filter_cb_pending_payment)
            }

            MyConstants.INVOICE_DESC_ESTADO_PLAN_PAGO -> this.descEstado =
                context.getString(R.string.fragment_filter_cb_payment_plan)

            else -> this.descEstado = descEstado
        }
    }

    override fun getImporteOrdenacion(): Float {
        return importeOrdenacion
    }

    fun getFecha(format: String?): String {
        return fecha.format(DateTimeFormatter.ofPattern(format))
    }

    override fun getFecha(): LocalDate {
        return fecha
    }

    override fun getTextColor(): Int? {
        return textColor
    }

    fun setTextColor(textColor: Int) {
        this.textColor = textColor
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(out: Parcel, flags: Int) {
        out.writeInt(id)
        out.writeString(descEstado)
        out.writeFloat(importeOrdenacion)
        out.writeString(fecha.toString())
        out.writeValue(textColor)
    }

    companion object CREATOR : Creator<InvoiceVO> {
        override fun createFromParcel(parcel: Parcel): InvoiceVO {
            return InvoiceVO(parcel)
        }

        override fun newArray(size: Int): Array<InvoiceVO?> {
            return arrayOfNulls(size)
        }
    }
}