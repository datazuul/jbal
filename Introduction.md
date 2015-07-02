# Introduction #

**jbal** is a library to manage bibliographic records. Records can be of different formats. Most common are ISO2709 syntax based records such as UNIMARC, MARC21, CDS-ISIS export format and many others. Emerging formats are based on XML such as XML-MARC, Dublin Core (DC), RDF based records and so on. Finally you can have raw text formats like SUTRS.

The pourpose of the library is to have a unique interface for all of them so you can access details with methods that hide the understating syntax (and sometime semantic).

In addition, the interface has the ability to compare records each other, returning a similarity index. In that way you can decide if two record could be two description of the same book or not.

# Details #

**jbal** is written in java.