package ttl.larku.tricky;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

interface Trick {
   public void doTrick();
}

@Component
@Profile("us-east")
@Qualifier("us-east")
class Trick1 implements Trick {
   @Override
   public void doTrick() {
      System.out.println("Handstand");
   }
}

@Component
@Profile("us-west")
@Qualifier("us-west")
class Trick2 implements Trick {
   @Override
   public void doTrick() {
      System.out.println("Cartwheel");
   }
}

//@Component
@Qualifier("us-west")
class Trick3 implements Trick {
   @Override
   public void doTrick() {
      System.out.println("Somersault");
   }
}

@Component
class Circus
{

   @Autowired
   private Trick trick;

   @Autowired
   @Qualifier("us-west")
   private List<Trick> allTricks;

   public void startShow() {
//      trick.doTrick();
      allTricks.forEach(Trick::doTrick);
   }

   public static void main(String[] args) {
      AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

      context.getEnvironment().setActiveProfiles("us-west");
      context.scan("ttl.larku.tricky");
      context.refresh();

      Circus circus = context.getBean("circus", Circus.class);
      circus.startShow();

      context.close();

      context.getEnvironment().setActiveProfiles("us-east");
      context.scan("ttl.larku.tricky");
      context.refresh();

      circus = context.getBean("circus", Circus.class);
      circus.startShow();


   }
}
