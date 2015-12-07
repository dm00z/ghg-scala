package model

case class IndirectData(electric: ElectricData,
                        gas: GasData,
                        material: MaterialData)
