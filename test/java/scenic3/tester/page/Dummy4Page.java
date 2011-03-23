package scenic3.tester.page;

import java.io.IOException;

import org.pirkaengine.core.PirkaException;
import org.slim3.controller.Navigation;

import scenic3.ScenicPage;
import scenic3.annotation.Default;
import scenic3.annotation.Page;

@Page("/")
public class Dummy4Page extends ScenicPage{
	@Default
	public Navigation index() throws PirkaException, IOException{
		return forward("");
	}

}
