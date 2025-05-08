public abstract class UpdatableDatabase<Model> extends Database<Model> {
    public UpdatableDatabase() {
        super();
    }

    public abstract boolean update(Model model);
    
}

