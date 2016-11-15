
package org.apache.chemistry.opencmis.commons.enums.transformers;

import javax.annotation.Generated;
import org.apache.chemistry.opencmis.commons.enums.UnfileObject;
import org.mule.api.transformer.DiscoverableTransformer;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractTransformer;
import org.mule.transformer.types.DataTypeFactory;

@Generated(value = "Mule DevKit Version 3.4.0", date = "2014-05-13T04:20:32-03:00", comments = "Build 3.4.0.1555.8df15c1")
public class UnfileObjectEnumTransformer
    extends AbstractTransformer
    implements DiscoverableTransformer
{

    private int weighting = DiscoverableTransformer.DEFAULT_PRIORITY_WEIGHTING;

    public UnfileObjectEnumTransformer() {
        registerSourceType(DataTypeFactory.create(String.class));
        setReturnClass(UnfileObject.class);
        setName("UnfileObjectEnumTransformer");
    }

    protected Object doTransform(Object src, String encoding)
        throws TransformerException
    {
        UnfileObject result = null;
        result = Enum.valueOf(UnfileObject.class, ((String) src));
        return result;
    }

    public int getPriorityWeighting() {
        return weighting;
    }

    public void setPriorityWeighting(int weighting) {
        this.weighting = weighting;
    }

}