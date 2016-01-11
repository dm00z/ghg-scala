package model

import diode.{ActionHandler, Circuit}
import ElectricData.CalcMethod
import diode.react.ReactConnector

object AppCircuit extends Circuit[GhgData] with ReactConnector[GhgData]{
  protected var model = SampleData.dataYenSo

  private val infoHandler = new ActionHandler(zoomRW(_.info)((d, v) => d.copy(info = v))) {
    def handle = {
      case x: Plant => updated(value.copy(f = x))
      case x: InfoData => updated(x)
    }
  }

  private val electricRw = zoomRW(_.indirect.electric)((d, v) => d.copy(indirect = d.indirect.copy(electric = v)))
  private val electricHandler = new ActionHandler(electricRw) {
    def handle = {
      case x: ElectricData.D1 => updated(value.copy(_1 = x))
      case x: ElectricData.D2 => updated(value.copy(_2 = x))
      case x: CalcMethod.Value => updated(value.copy(method = x))
    }
  }

  private val krRw = zoomRW(_.direct.relation)((d, v) => d.copy(direct = d.direct.copy(relation = v)))
  private val krHandler = new ActionHandler(krRw) {
    import KineticRelationData.Data
    def handle = {
      case (WaterType.Domestic, x: Data) => updated(value.copy(domestic = x))
      case (WaterType.Industrial, x: Data) => updated(value.copy(industrial = x))
    }
  }

  private val coefRw = zoomRW(_.direct.coef)((d, v) => d.copy(direct = d.direct.copy(coef = v)))
  private val coefHandler = new ActionHandler(coefRw) {
    import KineticCoefficientData._
    def handle = {
      case x: Aerobic => updated(value.copy(aerobic = x))
      case x: Nitrate => updated(value.copy(nitrate = x))
      case x: Anaerobic => updated(value.copy(anaerobic = x))
    }
  }
  private val directDataRw = zoomRW(_.direct.d)((d, v) => d.copy(direct = d.direct.copy(d = v)))
  private val directDataHandler = new ActionHandler(directDataRw) {
    import DirectTable._
    def handle = {
      case x: StreamInData => updated(value.copy(streamIn = x))
      case x: StreamOutData => updated(value.copy(streamOut = x))
      case x: PrimaryPoolData => updated(value.copy(primaryPool = x))
      case ("ae", x: PoolData) => updated(value.copy(aerobicPool = Some(x)))
      case ("ane", x: PoolData) => updated(value.copy(anaerobicPool = Some(x)))
      case ("decay", x: PoolData) => updated(value.copy(decayPool = x))
    }
  }

  private val ghgHandler = new ActionHandler(zoomRW(d => d)((d, v) => v)) {
    def handle = {
      case x: GhgData => updated(x)
    }
  }

  protected val actionHandler = combineHandlers(
    infoHandler, electricHandler, krHandler, coefHandler, directDataHandler, ghgHandler)
}
