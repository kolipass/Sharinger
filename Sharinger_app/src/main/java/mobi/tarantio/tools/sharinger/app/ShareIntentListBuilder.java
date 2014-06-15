package mobi.tarantio.tools.sharinger.app;

import android.content.pm.ResolveInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Билдер списка активити для шары
 * Created by kolipass on 15.06.14.
 */
public class ShareIntentListBuilder {
    private CheckDecorator checkDecorator = new CheckNonExported(new CheckSharingerPackage(null));

    public ShareIntentListBuilder() {
        CheckDecorator checkDecorator = new CheckNonExported(new CheckSharingerPackage(null));
    }

    /**
     * Конструктор на вырост. Если вдруг не устраивает стандартный декоратор
     *
     * @param checkDecorator
     */
    public ShareIntentListBuilder(CheckDecorator checkDecorator) {
        this.checkDecorator = checkDecorator;
    }

    public List<ResolveInfo> build(List<ResolveInfo> activities) {

        List<ResolveInfo> resolveInfos = new ArrayList<ResolveInfo>();
        for (ResolveInfo info : activities) {
            if (checkDecorator == null || checkDecorator.check(info)) {
                resolveInfos.add(info);
            }
        }
        return resolveInfos;
    }

    interface Checker {
        public boolean check(ResolveInfo info);
    }

    abstract class CheckDecorator implements Checker {
        protected Checker decorator;

        protected CheckDecorator(Checker decorator) {
            this.decorator = decorator;
        }

        public boolean check(ResolveInfo info) {
            return info != null && (decorator == null || decorator.check(info));
        }
    }

    /**
     * Служит, как отсеиватель не экспортируемых активити
     */
    class CheckNonExported extends CheckDecorator {
        protected CheckNonExported(Checker decorator) {
            super(decorator);
        }

        @Override
        public boolean check(ResolveInfo info) {
            return super.check(info) && info.activityInfo.exported;
        }
    }

    /**
     * Служит, как отсеиватель всех активити Sharinger'а
     */
    class CheckSharingerPackage extends CheckDecorator {

        protected CheckSharingerPackage(Checker decorator) {
            super(decorator);
        }

        @Override
        public boolean check(ResolveInfo info) {
            return super.check(info) && !info.activityInfo.packageName.contains("sharinger");
        }
    }
}