package model

case class IndirectData(electric: ElectricData = ElectricData(),
                        gas: GasData = GasData(),
                        material: MaterialData = MaterialData())
