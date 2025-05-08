import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public abstract class Database<Model> {
    private List<Model> models;

    Database() {
        loadPath(); // make sure that a db path is there, if it isnt declared as a constant
        models = retrieveAll();
    }

    protected abstract List<Model> retrieveAll();

    abstract public boolean save(Model m);

    public List<Model> getMatching(Predicate<Model> filter) {
        List<Model> matchingModels = new ArrayList<>();
        for (Model m : models) // checking models != null was done here on purpose to ensure functionallity at
                               // or close to compile time, check child implementations of retreiveAll()
        {
            if (filter.test(m)) {
                matchingModels.add(m);
            }
        }
        return matchingModels;
    }

    protected abstract void loadPath();
}
