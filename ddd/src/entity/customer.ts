class Customer {
  private readonly _id: string
  private readonly _name: string
  private readonly _address: string

  constructor(id: string, name: string, address: string) {
    this._id = id
    this._name = name
    this._address = address
  }

  get id() {
    return this._id
  }

  get name() {
    return this._name
  }

  get address() {
    return this._address
  }
}
