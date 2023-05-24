class Customer {
  private readonly _id: string
  private _name: string
  private readonly _address: string
  private _active = true

  constructor(id: string) {
    this._id = id
    this.validate()
  }

  validate() {
    if (!this._name) {
      throw new Error('Name is required')
    }
    if (!this._id) {
      throw new Error('Id is required')
    }
  }

  changeName(name: string) {
    this._name = name
  }

  activate() {
    this._active = true
  }

  desactivate() {
    this._active = false
  }
}

const customer = new Customer('1')
